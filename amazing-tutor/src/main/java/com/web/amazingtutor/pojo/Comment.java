package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer commentId;
    private Integer stuId;  // 外键对应student.stu_id
    private Integer tchId;  // 外键对应teacher.tch_id
    private Integer score;
    private String content;
}
