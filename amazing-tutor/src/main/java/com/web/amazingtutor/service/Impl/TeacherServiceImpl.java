package com.web.amazingtutor.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.amazingtutor.mapper.*;
import com.web.amazingtutor.pojo.Teacher;
import com.web.amazingtutor.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    @Transactional
    public boolean register(Teacher teacher) {
        return save(teacher);
    }

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
