package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("teacher")
public class Teacher {
    @TableId(type = IdType.AUTO)
    private Integer tchId;
    private String username;
    private String password;
}
