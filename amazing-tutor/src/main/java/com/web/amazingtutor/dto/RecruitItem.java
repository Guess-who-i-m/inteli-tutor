package com.web.amazingtutor.dto;

import com.web.amazingtutor.pojo.Recruit;
import com.web.amazingtutor.pojo.RecruitDate;
import lombok.Data;

import java.util.List;

@Data
public class RecruitItem {
    private Recruit recruit;
    private List<RecruitDate> recruitDates;
}
