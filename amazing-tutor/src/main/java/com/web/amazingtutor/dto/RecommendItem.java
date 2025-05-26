package com.web.amazingtutor.dto;

import com.web.amazingtutor.pojo.Recommend;
import com.web.amazingtutor.pojo.RecommendDate;
import lombok.Data;

import java.util.List;

@Data
public class RecommendItem {
    private Recommend recommend;
    private List<RecommendDate> recommendDates;
}
