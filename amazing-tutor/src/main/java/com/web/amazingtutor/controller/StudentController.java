package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.pojo.Student;
import com.web.amazingtutor.pojo.Teacher;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.JwtUtil;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;

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
}
