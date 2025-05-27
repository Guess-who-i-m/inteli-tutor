package com.web.amazingtutor.service;

import com.web.amazingtutor.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface StudentService extends IService<Student> {
    @Transactional
    boolean register(Student student);
}
