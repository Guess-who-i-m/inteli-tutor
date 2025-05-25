package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("recommend")
public class Recommend {
    @TableId(type = IdType.AUTO)
    private Integer recommendId;
    private Integer price;
    private String subject;
    private Boolean online;
    private String detail;
    private Integer tchId;  // 外键对应teacher.tch_id
}
