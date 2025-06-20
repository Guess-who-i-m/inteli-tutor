package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.dto.RecommendItem;
import com.web.amazingtutor.enums.DayEnum;
import com.web.amazingtutor.enums.SchLevelEnum;
import com.web.amazingtutor.enums.StuLevelEnum;
import com.web.amazingtutor.pojo.PageBean;
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
import java.util.*;
import java.util.stream.Collectors;

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
        int recommendId = 0;

        log.info(json);

        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(json);
            recommendId = jsonNode.get("recommend_id").asInt();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

      // 删除日期记录
        QueryWrapper<RecommendDate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("recommend_id", recommendId);
        recommendDateService.remove(queryWrapper);

        // 删除自荐记录
        recommendService.removeById(recommendId);

        return R.successMsg("删除成功");
    }

    @GetMapping("getMyRecommends")
    public R<PageBean<RecommendItem>> getMyRecommends(Integer pageNum, Integer pageSize, String subject, String online) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userId = map.get("id"); // 建议检查 userId 是否为 null 或类型是否正确

        // 1. 分页查询主表 Recommend
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<Recommend> recommendQueryWrapper = new QueryWrapper<>();
        recommendQueryWrapper.eq("tch_id", userId);
        if (subject != null) {
            recommendQueryWrapper.eq("subject", subject);
        }
        if (online != null) {
            Boolean bool = null;
            if (online.equals("true")) {
                bool = true;
            } else if (online.equals("false")) {
                bool = false;
            } else {
                return R.error("错误的线上线下格式");
            }
            recommendQueryWrapper.eq("online", bool);
        }
        List<Recommend> pagedRecommends = recommendService.list(recommendQueryWrapper); // pagedRecommends 现在是 Page<Recommend> 类型

        List<RecommendItem> recommendItemsResultList = new ArrayList<>();
        long totalRecords = 0;

        if (pagedRecommends instanceof Page) {
            totalRecords = ((Page<Recommend>) pagedRecommends).getTotal();
        } else {
            // 如果不是 Page 类型，可能 PageHelper 没有正确工作或 service.list() 返回的不是直接的 mapper 调用结果
            // 在这种情况下，totalRecords 可能需要另外查询或默认为当前列表大小（不准确）
            totalRecords = pagedRecommends.size(); // 这是一个不准确的回退
        }


        if (!pagedRecommends.isEmpty()) {
            // 2. 收集所有 pagedRecommends 的 recommend_id
            List<Integer> recommendIds = pagedRecommends.stream()
                    .map(Recommend::getRecommendId)
                    .collect(Collectors.toList());

            // 3. 一次性查询所有相关的 RecommendDate
            Map<Integer, List<RecommendDate>> datesByRecommendIdMap = new HashMap<>();
            if (!recommendIds.isEmpty()) {
                QueryWrapper<RecommendDate> dateQueryWrapper = new QueryWrapper<>();
                dateQueryWrapper.in("recommend_id", recommendIds); // 使用 IN 查询
                List<RecommendDate> allRecommendDates = recommendDateService.list(dateQueryWrapper);

                // 将 RecommendDate 按 recommend_id 分组
                for (RecommendDate rd : allRecommendDates) {
                    datesByRecommendIdMap.computeIfAbsent(rd.getRecommendId(), k -> new ArrayList<>()).add(rd);
                }
            }

            // 4. 组装 RecommendItem
            for (Recommend recommend : pagedRecommends) {
                RecommendItem item = new RecommendItem();
                item.setRecommend(recommend);
                // 从 Map 中获取对应的 RecommendDate列表，如果不存在则给一个空列表
                item.setRecommendDates(datesByRecommendIdMap.getOrDefault(recommend.getRecommendId(), Collections.emptyList()));
                recommendItemsResultList.add(item);
            }
        }

        PageBean<RecommendItem> pageBean = new PageBean<>();
        pageBean.setTotal(totalRecords);
        pageBean.setItems(recommendItemsResultList); // recommendItemsResultList 只包含当前页的数据

        return R.success(pageBean);
    }


    @GetMapping("/getAllRecommends")
    public R<PageBean<RecommendItem>> getAllRecommends(Integer pageNum, Integer pageSize, String subject, String online) {

        List<Recommend> pagedRecommends = null;

        // 1. 分页查询主表 Recommend
        PageHelper.startPage(pageNum, pageSize);

        if (subject == null && online == null) {
            pagedRecommends = recommendService.list();  // pagedRecommends 现在是 Page<Recruit> 类型
        } else {
            QueryWrapper<Recommend> queryWrapper = new QueryWrapper<>();
            if (subject != null) {
                queryWrapper.eq("subject", subject);
            }
            if (online != null) {
                Boolean bool = null;
                if (online.equals("true")) {
                    bool = true;
                } else if (online.equals("false")) {
                    bool = false;
                } else {
                    return R.error("错误的线上线下格式");
                }
                queryWrapper.eq("online", bool);
            }
            pagedRecommends = recommendService.list(queryWrapper);  // pagedRecommends 现在是 Page<Recruit> 类型
        }

        List<RecommendItem> recommendItemsResultList = new ArrayList<>();
        long totalRecords = 0;

        if (pagedRecommends instanceof Page) {
            totalRecords = ((Page<Recommend>) pagedRecommends).getTotal();
        } else {
            // 如果不是 Page 类型，可能 PageHelper 没有正确工作或 service.list() 返回的不是直接的 mapper 调用结果
            // 在这种情况下，totalRecords 可能需要另外查询或默认为当前列表大小（不准确）
            totalRecords = pagedRecommends.size(); // 这是一个不准确的回退
        }


        if (!pagedRecommends.isEmpty()) {
            // 2. 收集所有 pagedRecommends 的 recommend_id
            List<Integer> recommendIds = pagedRecommends.stream()
                    .map(Recommend::getRecommendId)
                    .collect(Collectors.toList());

            // 3. 一次性查询所有相关的 RecommendDate
            Map<Integer, List<RecommendDate>> datesByRecommendIdMap = new HashMap<>();
            if (!recommendIds.isEmpty()) {
                QueryWrapper<RecommendDate> dateQueryWrapper = new QueryWrapper<>();
                dateQueryWrapper.in("recommend_id", recommendIds); // 使用 IN 查询
                List<RecommendDate> allRecommendDates = recommendDateService.list(dateQueryWrapper);

                // 将 RecommendDate 按 recommend_id 分组
                for (RecommendDate rd : allRecommendDates) {
                    datesByRecommendIdMap.computeIfAbsent(rd.getRecommendId(), k -> new ArrayList<>()).add(rd);
                }
            }

            // 4. 组装 RecommendItem
            for (Recommend recommend : pagedRecommends) {
                RecommendItem item = new RecommendItem();
                item.setRecommend(recommend);
                // 从 Map 中获取对应的 RecommendDate列表，如果不存在则给一个空列表
                item.setRecommendDates(datesByRecommendIdMap.getOrDefault(recommend.getRecommendId(), Collections.emptyList()));
                recommendItemsResultList.add(item);
            }
        }

        PageBean<RecommendItem> pageBean = new PageBean<>();
        pageBean.setTotal(totalRecords);
        pageBean.setItems(recommendItemsResultList); // recommendItemsResultList 只包含当前页的数据

        return R.success(pageBean);
    }
}
