package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.web.amazingtutor.enums.SchLevelEnum;
import com.web.amazingtutor.enums.StuLevelEnum;
import lombok.Data;


@Data
@TableName("recruit")
public class Recruit {
    @TableId(type = IdType.AUTO)
    private Integer recruitId;
    private Integer price;
    private SchLevelEnum schLevel;
    private StuLevelEnum stuLevel;
    private String subject;
    private Boolean online;
    private String detail;
    private Integer stuId;  // 外键对应student.stu_id
}
