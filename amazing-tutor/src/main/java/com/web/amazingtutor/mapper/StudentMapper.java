package com.web.amazingtutor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.amazingtutor.pojo.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {
}
