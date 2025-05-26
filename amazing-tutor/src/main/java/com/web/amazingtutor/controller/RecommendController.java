package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.dto.RecommendItem;
import com.web.amazingtutor.enums.DayEnum;
import com.web.amazingtutor.enums.SchLevelEnum;
import com.web.amazingtutor.enums.StuLevelEnum;
import com.web.amazingtutor.pojo.Recommend;
import com.web.amazingtutor.pojo.RecommendDate;
import com.web.amazingtutor.pojo.Recruit;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;

    @PostMapping("/publishRecommend")
    @Transactional
    public R<String> publishRecommend(@RequestBody String json) throws JsonProcessingException {

        Map<String, Object> map = ThreadLocalUtil.get();

        int price = 0;
        String subject = null;
        boolean online = false;
        String detail = null;

        int timeNum = 0;
        List<String> days = new ArrayList<>();
        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        price = jsonNode.get("price").asInt();
        subject = jsonNode.get("subject").asText();
        online = jsonNode.get("online").asBoolean();
        detail = jsonNode.get("detail").asText();
        timeNum = jsonNode.get("time_num").asInt();

        JsonNode daysNode = jsonNode.get("days");
        if (daysNode != null && daysNode.isArray()) {
            for (JsonNode dayNode : daysNode) {
                days.add(dayNode.asText());
            }
        }

        JsonNode startTimesNode = jsonNode.get("start_times");
        if (startTimesNode != null && startTimesNode.isArray()) {
            for (JsonNode startTime : startTimesNode) {
                startTimes.add(LocalTime.parse(startTime.asText()));
            }
        }

        JsonNode endTimesNode = jsonNode.get("end_times");
        if (endTimesNode != null && endTimesNode.isArray()) {
            for (JsonNode endTime : endTimesNode) {
                endTimes.add(LocalTime.parse(endTime.asText()));
            }
        }

        Recommend recommend = new Recommend();
        recommend.setPrice(price);
        recommend.setSubject(subject);
        recommend.setOnline(online);
        recommend.setDetail(detail);
        recommend.setTchId((Integer) map.get("id"));

        recommendService.save(recommend);

        // 接下来处理时间相关的逻辑
        for(int i = 0 ; i < timeNum; i ++){
            RecommendDate recommendDate = new RecommendDate();
            recommendDate.setStartTime(startTimes.get(i));
            recommendDate.setEndTime(endTimes.get(i));
            recommendDate.setDay(DayEnum.valueOf(days.get(i)));
            recommendDate.setRecommendId(recommend.getRecommendId());

            recommendDateService.save(recommendDate);
        }

        return R.successMsg("成功发布自荐信息");
    }

    @PostMapping("/updateRecommend")
    @Transactional
    public R<String> updateRecommend(@RequestBody String json) throws JsonProcessingException {
        Map<String, Object> map = ThreadLocalUtil.get();

        int recommendId = 0;
        int price = 0;
        String subject = null;
        boolean online = false;
        String detail = null;

        int timeNum = 0;
        List<String> days = new ArrayList<>();
        List<LocalTime> startTimes = new ArrayList<>();
        List<LocalTime> endTimes = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        recommendId = jsonNode.get("recommend_id").asInt();
        price = jsonNode.get("price").asInt();
        subject = jsonNode.get("subject").asText();
        online = jsonNode.get("online").asBoolean();
        detail = jsonNode.get("detail").asText();
        timeNum = jsonNode.get("time_num").asInt();

        JsonNode daysNode = jsonNode.get("days");
        if (daysNode != null && daysNode.isArray()) {
            for (JsonNode dayNode : daysNode) {
                days.add(dayNode.asText());
            }
        }

        JsonNode startTimesNode = jsonNode.get("start_times");
        if (startTimesNode != null && startTimesNode.isArray()) {
            for (JsonNode startTime : startTimesNode) {
                startTimes.add(LocalTime.parse(startTime.asText()));
            }
        }

        JsonNode endTimesNode = jsonNode.get("end_times");
        if (endTimesNode != null && endTimesNode.isArray()) {
            for (JsonNode endTime : endTimesNode) {
                endTimes.add(LocalTime.parse(endTime.asText()));
            }
        }

        // 校验时间参数数量一致性
        if (days.size() != timeNum || startTimes.size() != timeNum || endTimes.size() != timeNum) {
            return R.error("时间参数数量不匹配");
        }

        Recommend recommend = new Recommend();
        recommend.setRecommendId(recommendId);
        recommend.setPrice(price);
        recommend.setSubject(subject);
        recommend.setOnline(online);
        recommend.setDetail(detail);
        recommend.setTchId((Integer) map.get("id"));

        recommendService.updateById(recommend);

        // 接下来处理时间相关逻辑
        QueryWrapper<RecommendDate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recommend_id", recommendId);
        recommendDateService.remove(queryWrapper);

        List<RecommendDate> newDates = new ArrayList<>();
        for (int i = 0; i < timeNum; i++) {
            RecommendDate date = new RecommendDate();
            date.setRecommendId(recommendId);
            date.setDay(DayEnum.valueOf(days.get(i)));
            date.setStartTime(startTimes.get(i));
            date.setEndTime(endTimes.get(i));
            newDates.add(date);
        }

        if (!newDates.isEmpty()) {
            recommendDateService.saveBatch(newDates);
        }

        return R.successMsg("更新成功");
    }

    @PostMapping("/deleteRecommend")
    public R<String> deleteRecommend(@RequestBody String json) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.convertValue(json, JsonNode.class);
        int recommendId = jsonNode.get("recommend_id").asInt();

        // 删除日期记录
        QueryWrapper<RecommendDate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recommend_id", recommendId);
        recommendDateService.remove(queryWrapper);

        // 删除自荐记录
        recommendService.removeById(recommendId);

        return R.successMsg("删除成功");
    }

    @GetMapping("getMyRecommends")
    public R<List<RecommendItem>> getMyRecommends() {
        Map<String,Object> map = ThreadLocalUtil.get();
        ObjectMapper mapper = new ObjectMapper();

        QueryWrapper<Recommend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tch_id", map.get("id"));
        List<Recommend> recommends = recommendService.list(queryWrapper);

        List<RecommendItem> recommendItems = new ArrayList<>();

        int recommendId = 0;
        List<RecommendDate> recommendDates;

        for (Recommend recommend : recommends) {
            RecommendItem item = new RecommendItem();
            recommendId = recommend.getRecommendId();

            QueryWrapper<RecommendDate> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("recommend_id", recommendId);
            recommendDates = recommendDateService.list(queryWrapper1);

            item.setRecommend(recommend);
            if (!recommendDates.isEmpty()) {
                item.setRecommendDates(recommendDates);
            }

            recommendItems.add(item);
        }

        return R.success(recommendItems);
    }

    @GetMapping("/getAllRecommends")
    public R<List<RecommendItem>> getAllRecommends() {
        ObjectMapper mapper = new ObjectMapper();

        List<Recommend> recommends = recommendService.list();

        List<RecommendItem> recommendItems = new ArrayList<>();

        int recommendId = 0;
        List<RecommendDate> recommendDates;

        for (Recommend recommend : recommends) {
            RecommendItem item = new RecommendItem();
            recommendId = recommend.getRecommendId();

            QueryWrapper<RecommendDate> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("recommend_id", recommendId);
            recommendDates = recommendDateService.list(queryWrapper1);

            item.setRecommend(recommend);
            if (!recommendDates.isEmpty()) {
                item.setRecommendDates(recommendDates);
            }

            recommendItems.add(item);
        }

        return R.success(recommendItems);
    }
}
