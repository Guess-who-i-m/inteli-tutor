package com.web.amazingtutor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.amazingtutor.common.R;
import com.web.amazingtutor.enums.EduBgEnum;
import com.web.amazingtutor.enums.GenderEnum;
import com.web.amazingtutor.enums.SchLevelEnum;
import com.web.amazingtutor.pojo.StuInfo;
import com.web.amazingtutor.pojo.TchInfo;
import com.web.amazingtutor.service.*;
import com.web.amazingtutor.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/tchInfo")
@RequiredArgsConstructor
public class TchInfoController {
    private final TeacherService teacherService;
    private final TchInfoService tchInfoService;
    private final StuInfoService stuInfoService;
    private final StudentService studentService;
    private final RecruitDateService recruitDateService;
    private final RecruitService recruitService;
    private final RecommendDateService recommendDateService;
    private final RecommendService recommendService;
    private final CommentService commentService;

    @PostMapping("/uploadMsg")
    public R<String> uploadTchMsg(@RequestBody String json){

        // 从threadLocal中获得token
        Map<String, Object> map = ThreadLocalUtil.get();
        if (!map.get("type").equals("teacher")) {
            return R.error("请以教师身份登录");
        }

        // 声明从json中获取字段的变量
        String gender;
        String school;
        String schLevel;
        String eduBg;
        String phoneNum;
        String avatar;

        // 从json中获得变量的值
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            gender = jsonNode.get("gender").asText();
            school = jsonNode.get("school").asText();
            schLevel = jsonNode.get("schLevel").asText();
            eduBg = jsonNode.get("eduBg").asText();
            phoneNum = jsonNode.get("phoneNum").asText();
            avatar = jsonNode.get("avatar").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 构造tchInfo对象
        TchInfo tchInfo = new TchInfo();
        tchInfo.setGender(GenderEnum.valueOf(gender));
        tchInfo.setSchool(school);
        tchInfo.setSchLevel(SchLevelEnum.valueOf(schLevel));
        tchInfo.setEduBg(EduBgEnum.valueOf(eduBg));
        tchInfo.setPhoneNum(phoneNum);
        tchInfo.setAvatar(avatar);
        tchInfo.setTchId((Integer) map.get("id"));

        // 在现有的tch_info表单中查找相关的内容
        if (map.get("id") != null)
        {
            QueryWrapper<TchInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tch_id", map.get("id"));
            TchInfo res = tchInfoService.getOne(queryWrapper);
            if (res != null){
                // 之前能查到tchInfo
                tchInfo.setInfoId(res.getInfoId());
                tchInfoService.updateById(tchInfo);
                return R.successMsg("更新成功！");
            } else {
                // 之前没存过tchInfo
                tchInfoService.save(tchInfo);
                return R.successMsg("上传成功");
            }
        } else {
            return R.error("token中不含有id信息");
        }
    }

    @GetMapping("getTchMsg")
    public R<TchInfo> getTchMsg(){
        // 从threadLocal里面获得键值对
        Map<String, Object> map = ThreadLocalUtil.get();
        if (!map.get("type").equals("teacher")) {
            return R.error("请以教师身份登录");
        }

        // 查询对应tch_id的信息
        QueryWrapper<TchInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tch_id", map.get("id"));
        TchInfo res = tchInfoService.getOne(queryWrapper);

        // 将查询结果返回
        return R.success(res);
    }

    @PostMapping("/updateAvatar")
    public R<String> updateAvatar(@RequestBody String json) throws JsonProcessingException {
        Map<String, Object> map = ThreadLocalUtil.get();

        String avatar;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;

        jsonNode = mapper.readTree(json);
        System.out.println(json);
        avatar = jsonNode.get("avatar").asText();

        int tchId = (int) map.get("id");

        QueryWrapper<TchInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tch_id", tchId);
        TchInfo res = tchInfoService.getOne(queryWrapper);

        if (res != null) {
            res.setAvatar(avatar);
        } else {
            return R.error("请先登记个人信息后再尝试修改头像哦");
        }
        tchInfoService.updateById(res);

        return R.successMsg("更新成功");

    }
}
