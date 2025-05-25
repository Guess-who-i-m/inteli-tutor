package com.web.amazingtutor.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.web.amazingtutor.enums.DayEnum;
import lombok.Data;

import java.time.LocalTime;

@Data
@TableName("recommend_date")
public class RecommendDate {
    @TableId(type = IdType.AUTO)
    private Integer dateId;
    private DayEnum day;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer recommendId;  // 外键对应recommend.recommend_id
}
