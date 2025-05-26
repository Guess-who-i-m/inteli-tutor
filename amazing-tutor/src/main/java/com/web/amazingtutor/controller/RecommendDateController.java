package com.web.amazingtutor.controller;

import com.web.amazingtutor.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/recommendDate")
@RequiredArgsConstructor
public class RecommendDateController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;
}
