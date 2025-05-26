package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.web.amazingtutor.enums.GenderEnum;
import lombok.Data;

@Data
@TableName("stu_info")
public class StuInfo {
    @TableId(type = IdType.AUTO)
    private Integer infoId;
    private GenderEnum gender;
    private Integer grade;
    private String addr;
    private String phoneNum;
    private String avatar;
    private Integer stuId;  // 外键对应student.stu_id
}
