package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.dto.RecommendItem;
import com.web.amazingtutor.dto.RecruitItem;
import com.web.amazingtutor.enums.DayEnum;
import com.web.amazingtutor.enums.SchLevelEnum;
import com.web.amazingtutor.enums.StuLevelEnum;
import com.web.amazingtutor.pojo.*;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;


    @PostMapping("/publishRecruit")
    @Transactional
    public R<String> publishRecruit(@RequestBody String json) {

        Map<String, Object> map = ThreadLocalUtil.get();

        int price;
        String schLevel = null;
        String stuLevel = null;
        String subject=null;
        boolean online = false;
        String detail = null;
        int timeNum = 0;
        List<String> days = new ArrayList<>();
        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = mapper.readTree(json);
            price = jsonNode.get("price").asInt();
            schLevel = jsonNode.get("schLevel").asText();
            stuLevel = jsonNode.get("stuLevel").asText();
            subject = jsonNode.get("subject").asText();
            online = jsonNode.get("online").asBoolean();
            detail = jsonNode.get("detail").asText();
            timeNum = jsonNode.get("time_num").asInt();

            // 处理时间列表
            JsonNode daysNode = jsonNode.get("days");
            if (daysNode != null && daysNode.isArray()) {
                for (JsonNode dayNode : daysNode) {
                    days.add(dayNode.asText());
                }
            }

            JsonNode startTimesNode = jsonNode.get("start_times");
            if (startTimesNode != null) {
                for (JsonNode startTime : startTimesNode) {
                    startTimes.add(LocalTime.parse(startTime.asText()));
                }
            }

            JsonNode endTimesNode = jsonNode.get("end_times");
            if (endTimesNode != null) {
                for (JsonNode endTime : endTimesNode) {
                    endTimes.add(LocalTime.parse(endTime.asText()));
                }
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Recruit recruit = new Recruit();
        recruit.setPrice(price);
        recruit.setSchLevel(SchLevelEnum.valueOf(schLevel));
        recruit.setStuLevel(StuLevelEnum.valueOf(stuLevel));
        recruit.setSubject(subject);
        recruit.setOnline(online);
        recruit.setDetail(detail);
        recruit.setStuId((Integer) map.get("id"));

        recruitService.save(recruit);

        // 接下来处理时间相关的逻辑
        for(int i = 0; i < timeNum; i++) {
            RecruitDate recruitDate = new RecruitDate();
            recruitDate.setStartTime(startTimes.get(i));
            recruitDate.setEndTime(endTimes.get(i));
            recruitDate.setDay(DayEnum.valueOf(days.get(i)));
            recruitDate.setRecruitId(recruit.getRecruitId());

            recruitDateService.save(recruitDate);
        }

        return R.successMsg("成功发布招聘信息");

    }

    @PostMapping("/updateRecruit")
    @Transactional
    public R<String> updateRecruit(@RequestBody String json) {
        Map<String, Object> map = ThreadLocalUtil.get();

        int recruitId = 0;
        int price = 0;
        String schLevel = null;
        String stuLevel = null;
        String subject=null;
        boolean online = false;
        String detail = null;
        int timeNum = 0;
        List<String> days = new ArrayList<>();
        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(json);
            recruitId = jsonNode.get("recruit_id").asInt();
            price = jsonNode.get("price").asInt();
            schLevel = jsonNode.get("sch_level").asText();
            stuLevel = jsonNode.get("stu_level").asText();
            subject = jsonNode.get("subject").asText();
            online = jsonNode.get("online").asBoolean();
            detail = jsonNode.get("detail").asText();
            timeNum = jsonNode.get("time_num").asInt();

            // 处理时间列表
            JsonNode daysNode = jsonNode.get("days");
            if (daysNode != null && daysNode.isArray()) {
                for (JsonNode dayNode : daysNode) {
                    days.add(dayNode.asText());
                }
            }

            JsonNode startTimesNode = jsonNode.get("start_times");
            if (startTimesNode != null) {
                for (JsonNode startTime : startTimesNode) {
                    startTimes.add(LocalTime.parse(startTime.asText()));
                }
            }

            JsonNode endTimesNode = jsonNode.get("end_times");
            if (endTimesNode != null) {
                for (JsonNode endTime : endTimesNode) {
                    endTimes.add(LocalTime.parse(endTime.asText()));
                }
            }

            // 校验时间参数数量一致性
            if (days.size() != timeNum || startTimes.size() != timeNum || endTimes.size() != timeNum) {
                return R.error("时间参数数量不匹配");
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Recruit recruit = new Recruit();
        recruit.setRecruitId(recruitId);
        recruit.setPrice(price);
        recruit.setSchLevel(SchLevelEnum.valueOf(schLevel));
        recruit.setStuLevel(StuLevelEnum.valueOf(stuLevel));
        recruit.setSubject(subject);
        recruit.setOnline(online);
        recruit.setDetail(detail);
        recruit.setStuId((Integer) map.get("id"));

        recruitService.updateById(recruit);

        // 接下来处理时间相关逻辑
        QueryWrapper<RecruitDate> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("recruit_id", recruitId);
        recruitDateService.remove(deleteWrapper);
        List<RecruitDate> newDates = new ArrayList<>();
        for (int i = 0; i < timeNum; i++) {
            RecruitDate date = new RecruitDate();
            date.setRecruitId(recruitId);
            date.setDay(DayEnum.valueOf(days.get(i))); // 假设DayEnum处理星期几
            date.setStartTime(startTimes.get(i));
            date.setEndTime(endTimes.get(i));
            newDates.add(date);
        }

        if (!newDates.isEmpty()) {
            recruitDateService.saveBatch(newDates);
        }


        return R.successMsg("更新成功");

    }

    @PostMapping("/deleteRecruit")
    public R<String> deleteRecruit(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        int recruitId = 0;

        try {
            jsonNode = mapper.readTree(json);
            recruitId = jsonNode.get("recruit_id").asInt();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 先删除日期记录
        QueryWrapper<RecruitDate> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("recruit_id", recruitId);
        recruitDateService.remove(deleteWrapper);
        // 在删除招聘记录
        recruitService.removeById(recruitId);

        return R.successMsg("删除成功");
    }

    @GetMapping("getMyRecruits")
    public R<PageBean<RecruitItem>> getMyRecruits(Integer pageNum, Integer pageSize) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userId = map.get("id"); // 建议检查 userId 是否为 null 或类型是否正确

        // 1. 分页查询主表 Recommend
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<Recruit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_id", userId);
        List<Recruit> pagedRecruits = recruitService.list(queryWrapper);  // pagedRecruits 现在是 Page<Recruit> 类型

        List<RecruitItem> recruitItemsResultList = new ArrayList<>();
        long totalRecords = 0;

        if (pagedRecruits instanceof Page) {
            totalRecords = ((Page<Recruit>) pagedRecruits).getTotal();
        } else {
            // 如果不是 Page 类型，可能 PageHelper 没有正确工作或 service.list() 返回的不是直接的 mapper 调用结果
            // 在这种情况下，totalRecords 可能需要另外查询或默认为当前列表大小（不准确）
            totalRecords = pagedRecruits.size(); // 这是一个不准确的回退
        }


        if (!pagedRecruits.isEmpty()) {
            // 2. 收集所有 pagedRecommends 的 recommend_id
            List<Integer> recruitIds = pagedRecruits.stream()
                    .map(Recruit::getRecruitId)
                    .collect(Collectors.toList());

            // 3. 一次性查询所有相关的 RecommendDate
            Map<Integer, List<RecruitDate>> datesByRecruitIdMap = new HashMap<>();
            if (!recruitIds.isEmpty()) {
                QueryWrapper<RecruitDate> dateQueryWrapper = new QueryWrapper<>();
                dateQueryWrapper.in("recruit_id", recruitIds); // 使用 IN 查询
                List<RecruitDate> allRecruitDates = recruitDateService.list(dateQueryWrapper);

                // 将 RecommendDate 按 recommend_id 分组
                for (RecruitDate rd : allRecruitDates) {
                    datesByRecruitIdMap.computeIfAbsent(rd.getRecruitId(), k -> new ArrayList<>()).add(rd);
                }
            }

            // 4. 组装 RecommendItem
            for (Recruit recruit : pagedRecruits) {
                RecruitItem item = new RecruitItem();
                item.setRecruit(recruit);
                // 从 Map 中获取对应的 RecommendDate列表，如果不存在则给一个空列表
                item.setRecruitDates(datesByRecruitIdMap.getOrDefault(recruit.getRecruitId(), Collections.emptyList()));
                recruitItemsResultList.add(item);
            }
        }

        PageBean<RecruitItem> pageBean = new PageBean<>();
        pageBean.setTotal(totalRecords);
        pageBean.setItems(recruitItemsResultList); // recommendItemsResultList 只包含当前页的数据

        return R.success(pageBean);
    }

    @GetMapping("/getAllRecruits")
    public R<List<RecruitItem>> getAllRecruits() {

        List<Recruit> recruits = recruitService.list();

        List<RecruitItem> recruitItems = new ArrayList<>();

        int recruitId = 0;
        List<RecruitDate> recruitDates;
        for (Recruit recruit: recruits) {
            RecruitItem item = new RecruitItem();
            recruitId = recruit.getRecruitId();

            QueryWrapper<RecruitDate> recruitDateQueryWrapper = new QueryWrapper<>();
            recruitDateQueryWrapper.eq("recruit_id", recruitId);
            recruitDates = recruitDateService.list(recruitDateQueryWrapper);

            item.setRecruit(recruit);
            if (!recruitDates.isEmpty()) {
                item.setRecruitDates(recruitDates);
            }


            recruitItems.add(item);
        }

        return R.success(recruitItems);
    }
}
