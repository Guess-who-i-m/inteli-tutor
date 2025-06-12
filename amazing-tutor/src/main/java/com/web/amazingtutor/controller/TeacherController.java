package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.pojo.Recommend;
import com.web.amazingtutor.pojo.Student;
import com.web.amazingtutor.pojo.TchInfo;
import com.web.amazingtutor.pojo.Teacher;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.JwtUtil;
import com.web.amazingtutor.utils.LLMUtil;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;
    private final ChatClient chatClient;

    @PostMapping("/register")
    public R<Student> register(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode;
        String username = null;
        String password = null;

        try{
            jsonNode = mapper.readTree(json);
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Teacher teacher = teacherService.getOne(queryWrapper);

        if (teacher != null) {
            return R.error("该用户名已被注册");
        }

        teacher = new Teacher();
        teacher.setUsername(username);
        teacher.setPassword(password);
        teacherService.save(teacher);

        return R.successMsg("注册成功");
    }

    @PostMapping("/login")
    public R<String> teacherLogin(@RequestBody String json) {

        ObjectMapper mapper = new ObjectMapper();
        String username = null;
        String password = null;

        JsonNode jsonNode = null;

        try {
            jsonNode = mapper.readTree(json);
            username = jsonNode.get("username").asText();
            password = jsonNode.get("password").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Teacher res = teacherService.getOne(queryWrapper);

        if (res != null) {
            if (res.getPassword().equals(password)) {
                // 登录成功
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                claims.put("password", password);
                claims.put("id", res.getTchId());
                claims.put("type", "teacher");

                String token = JwtUtil.genToken(claims);

                return R.success(token);
            }
            else {
                return R.error("密码错误，请重新尝试");
            }
        }
        else {
            return R.error("不存在的用户");
        }
    }

    @PostMapping("/llmRecommendTeacher")
    public R<List<Recommend>> llmRecommendTeacher(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        String description = null;

        try {
            jsonNode = mapper.readTree(json);
            description = jsonNode.get("prompt").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String application = "现在你需要为学生和家长推荐家教老师。你将会得到三个依据，分别是：" +
            "学生和家长对自身情况的描述、数据库中存储的全部应聘信息、数据库中存储的全部家教老师信息。" +
            "你需要依据这三者之间的内容，匹配出最适合学生的家教老师。" +
            "注意，你需要格外注意应聘信息和家教老师信息数据中，teacher_id的匹配，严格按照数据库信息输出";

        String rules = "你需要以Json格式进行回复，要求格式形如{teacher:[1,2,3],reason：\"<为什么选择这些老师的原因>\"}，" +
            "其中1，2和3都是老师的编号。不允许输出其它额外内容。" ;

        List<TchInfo> teachers = tchInfoService.list();
        List<Recommend> recommends = recommendService.list();
        // 创建按tch_id分组的推荐信息Map
        Map<Integer, List<Recommend>> recommendMap = recommends.stream()
            .collect(Collectors.groupingBy(Recommend::getTchId));
        // 构建连接后的信息字符串
        StringBuilder combinedInfo = new StringBuilder();
        for (TchInfo teacher : teachers) {
            Integer tchId = teacher.getTchId();
            List<Recommend> teacherRecommends = recommendMap.getOrDefault(tchId, Collections.emptyList());

            combinedInfo.append("老师ID: ").append(tchId).append("\n");
            combinedInfo.append("老师信息: ").append(teacher.toString()).append("\n");
            combinedInfo.append("关联推荐: ").append(teacherRecommends.toString()).append("\n\n");
        }
        String userPrompt = application + "\n"
            + "学生和家长对自身情况的描述:" + "\n"
            + description + "\n"
            + "数据库中的教师信息及关联推荐（按tch_id连接）:" + "\n"  // 修改标题
            + combinedInfo.toString()  // 使用连接后的数据
            + rules;

        log.info(userPrompt);

        String response = chatClient
            .prompt()
            .user(userPrompt)
            .call()
            .content();

        List<Integer> recommendedTeacherIds = new ArrayList<>();
        String reason;
        try {
            // Attempt to parse the LLM response as the expected JSON structure
            // The LLM might sometimes add ```json ... ``` markdown, try to strip it.
            String cleanedResponse = LLMUtil.cleanJsonString(response);
            JsonNode responseNode = mapper.readTree(cleanedResponse);
            JsonNode idsNode = responseNode.get("teacher"); // Match the key in 'rules'
            reason = responseNode.get("reason").asText();
            if (idsNode != null && idsNode.isArray()) {
                for (JsonNode idNode : idsNode) {
                    if (idNode.isInt()) {
                        recommendedTeacherIds.add(idNode.asInt());
                    } else {
                        System.err.println("LLM returned a non-integer ID in the list: " + idNode.asText());
                        // Decide how to handle: skip, error out, or try to parse
                    }
                }
            } else {
                System.err.println("LLM response did not contain 'teacher_ids' array or was not in expected format. Response: " + cleanedResponse);
                // You might want to try a more lenient parsing if the LLM is inconsistent
                // For example, if it sometimes returns just an array of IDs without the "teacher_ids" key.
                // Or if it uses a different key.
                return R.error( "LLM response format error. Could not extract teacher IDs.");
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing LLM JSON response: " + response + " - " + e.getMessage());
            return R.error("LLM response was not valid JSON or did not match expected structure.");
        }

        if (recommendedTeacherIds.isEmpty()) {
            return R.success(Collections.emptyList());
        }

        // 修复1：添加排序规则确保获取全部记录
        QueryWrapper<Recommend> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tch_id", recommendedTeacherIds)
            .orderByAsc("tch_id", "recommend_id"); // 添加排序

        List<Recommend> recommendedTeachers = recommendService.list(queryWrapper);
        // 修复2：按tch_id分组存储所有推荐记录
        Map<Integer, List<Recommend>> teacherGroupMap = new LinkedHashMap<>();
        for (Recommend teacher : recommendedTeachers) {
            teacherGroupMap
                .computeIfAbsent(teacher.getTchId(), k -> new ArrayList<>())
                .add(teacher);
        }
        // 修复3：保持原始顺序并收集所有推荐记录
        List<Recommend> sortedTeachers = new ArrayList<>();
        for (Integer id : recommendedTeacherIds) {
            if (teacherGroupMap.containsKey(id)) {
                sortedTeachers.addAll(teacherGroupMap.get(id));
            }
        }

        // 构建返回结果
        R<List<Recommend>> result = new R<>();
        result.add("reason", reason);
        result.setCode(1);
        result.setData(sortedTeachers); // 使用排序后的列表
        result.setMsg("查询成功");
        return result;
    }
}
