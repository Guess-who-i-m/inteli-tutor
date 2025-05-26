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
@RequestMapping("/api/stuInfo")
@RequiredArgsConstructor
public class StuInfoController {
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
    public R<String> uploadStuMsg(@RequestBody String json){

        // 从threadLocal中获得token
        Map<String, Object> map = ThreadLocalUtil.get();
        if (!map.get("type").equals("student")) {
            return R.error("请以学生身份登录");
        }

        // 声明从json中获取字段的变量
        String gender;
        int grade;
        String addr;
        String phoneNum;
        String avatar;

        // 从json中获得变量的值
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            gender = jsonNode.get("gender").asText();
            grade = jsonNode.get("grade").asInt();
            addr = jsonNode.get("addr").asText();
            phoneNum = jsonNode.get("phoneNum").asText();
            avatar = jsonNode.get("avatar").asText();
//            System.out.println(avatar);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 构造tchInfo对象
        StuInfo stuInfo = new StuInfo();
        stuInfo.setGender(GenderEnum.valueOf(gender));
        stuInfo.setGrade(grade);
        stuInfo.setAddr(addr);
        stuInfo.setPhoneNum(phoneNum);
        stuInfo.setAvatar(avatar);
        stuInfo.setStuId((Integer) map.get("id"));

        // 在现有的tch_info表单中查找相关的内容
        if (map.get("id") != null)
        {
            QueryWrapper<StuInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("stu_id", map.get("id"));
            StuInfo res = stuInfoService.getOne(queryWrapper);
            if (res != null){
                // 之前能查到tchInfo
                stuInfo.setInfoId(res.getInfoId());
                stuInfoService.updateById(stuInfo);
                return R.successMsg("更新成功！");
            } else {
                // 之前没存过tchInfo
                stuInfoService.save(stuInfo);
                return R.successMsg("上传成功");
            }
        } else {
            return R.error("token中不含有id信息");
        }
    }

    @GetMapping("getStuMsg")
    public R<StuInfo> getStuMsg(){
        // 从threadLocal里面获得键值对
        Map<String, Object> map = ThreadLocalUtil.get();
        if (!map.get("type").equals("student")) {
            System.out.println(map.get("type"));
            return R.error("请以学生身份登录");
        }

        // 查询对应tch_id的信息
        QueryWrapper<StuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_id", map.get("id"));
        StuInfo res = stuInfoService.getOne(queryWrapper);

        if (res == null){
            StuInfo stuInfo = new StuInfo();
            stuInfo.setStuId((Integer) map.get("id"));
            stuInfoService.save(stuInfo);
            res = stuInfoService.getOne(queryWrapper);
        }

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

        int stuId = (int) map.get("id");

        QueryWrapper<StuInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stu_id", stuId);
        StuInfo res = stuInfoService.getOne(queryWrapper);

        if (res != null) {
            res.setAvatar(avatar);
        } else {
            return R.error("请先登记个人信息后再尝试修改头像哦");
        }
        stuInfoService.updateById(res);

        return R.successMsg("更新成功");

    }
}
