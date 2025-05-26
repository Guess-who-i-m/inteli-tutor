package com.web.amazingtutor.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.amazingtutor.mapper.*;
import com.web.amazingtutor.pojo.Comment;
import com.web.amazingtutor.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final CommentMapper commentMapper;
    private final TeacherMapper teacherMapper;
    private final RecommendMapper recommendMapper;
    private final RecommendDateMapper recommendDateMapper;
    private final RecruitMapper recruitMapper;
    private final RecruitDateMapper recruitDateMapper;
    private final StudentMapper studentMapper;
    private final StuInfoMapper stuInfoMapper;
    private final TchInfoMapper tchInfoMapper;
}
