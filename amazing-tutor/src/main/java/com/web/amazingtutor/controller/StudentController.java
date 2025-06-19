package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.pojo.*;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.JwtUtil;
import com.web.amazingtutor.utils.LLMUtil;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final TeacherService teacherService;
    private final StuInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecruitDateService recommendDateService;
    private final RecruitService recommendService;
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

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Student student = studentService.getOne(queryWrapper);

        if (student != null) {
            return R.error("该用户名已被注册");
        }

        student = new Student();
        student.setUsername(username);
        student.setPassword(password);
        studentService.save(student);

        return R.successMsg("注册成功");
    }

    @PostMapping("/login")
    public R<String> studentLogin(@RequestBody String json) {

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

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Student res = studentService.getOne(queryWrapper);

        if (res != null) {
            if (res.getPassword().equals(password)) {
                // 登录成功
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                claims.put("password", password);
                claims.put("id", res.getStuId());
                claims.put("type", "student");

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

    @PostMapping("/llmRecommendStudent")
    public R<List<Recruit>> llmRecommendStudent(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        String description = null;

        try {
            jsonNode = mapper.readTree(json);
            description = jsonNode.get("prompt").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String application = "现在你需要为家教老师推荐学生招聘单。你将会得到三个依据，分别是：" +
            "家教老师对自身情况的描述、数据库中存储的全部招聘信息、数据库中存储的全部学生信息。" +
            "你需要依据这三者之间的内容，匹配出最适合家教老师的学生招聘单。" +
            "注意：你需要额外注意招聘信息和家长/学生信息中，stu_id的一致性匹配";

        String rules = "你需要以Json格式进行回复，要求格式形如{recruit:[1,2,3],reason：\"<推荐这些招聘单的原因>\"}，" +
            "其中1，2和3都是招聘的编号(recruit_id)。不允许输出其它额外内容。" ;

        List<StuInfo> teachers = tchInfoService.list();
        List<Recruit> recommends = recommendService.list();
        // 创建按tch_id分组的推荐信息Map
        Map<Integer, List<Recruit>> recommendMap = recommends.stream()
            .collect(Collectors.groupingBy(Recruit::getStuId));
        // 构建连接后的信息字符串
        StringBuilder combinedInfo = new StringBuilder();
        for (StuInfo teacher : teachers) {
            Integer tchId = teacher.getStuId();
            List<Recruit> teacherRecruits = recommendMap.getOrDefault(tchId, Collections.emptyList());

            combinedInfo.append("学生ID: ").append(tchId).append("\n");
            combinedInfo.append("学生信息: ").append(teacher.toString()).append("\n");
            combinedInfo.append("关联招聘信息: ").append(teacherRecruits.toString()).append("\n\n");
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

        System.out.println(response);

        List<Integer> recruitIds = new ArrayList<>();
        String reason;
        try {
            // Attempt to parse the LLM response as the expected JSON structure
            // The LLM might sometimes add ```json ... ``` markdown, try to strip it.
            String cleanedResponse = LLMUtil.cleanJsonString(response);
            JsonNode responseNode = mapper.readTree(cleanedResponse);
            JsonNode idsNode = responseNode.get("recruit"); // Match the key in 'rules'
            reason = responseNode.get("reason").asText();

            if (idsNode != null && idsNode.isArray()) {
                for (JsonNode idNode : idsNode) {
                    if (idNode.isInt()) {
                        recruitIds.add(idNode.asInt());
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

        if (recruitIds.isEmpty()) {

            return R.success(Collections.emptyList());
        }

        List<Recruit> recruits1 = recruitService.listByIds(recruitIds);
        R<List<Recruit>> result = new R<>();
        result.add("reason", reason);
        result.setCode(1);
        result.setData(recruits1);
        result.setMsg("查询成功");

        log.info("推荐结果{}", result);

        return result;
    }
}
