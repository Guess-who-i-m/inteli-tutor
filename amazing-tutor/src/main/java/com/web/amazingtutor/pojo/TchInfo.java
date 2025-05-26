package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.web.amazingtutor.enums.EduBgEnum;
import com.web.amazingtutor.enums.GenderEnum;
import com.web.amazingtutor.enums.SchLevelEnum;
import lombok.Data;

@Data
@TableName("tch_info")
public class TchInfo {
    @TableId(type = IdType.AUTO)
    private Integer infoId;
    private GenderEnum gender;
    private String school;
    private SchLevelEnum schLevel;
    private EduBgEnum eduBg;
    private String phoneNum;
    private String avatar;
    private Integer tchId;  // 外键对应teacher.tch_id
}
