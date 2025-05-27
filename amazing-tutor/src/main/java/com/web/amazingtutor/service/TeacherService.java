package com.web.amazingtutor.service;

import com.web.amazingtutor.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface TeacherService extends IService<Teacher> {
    @Transactional
    boolean register(Teacher teacher);
}

