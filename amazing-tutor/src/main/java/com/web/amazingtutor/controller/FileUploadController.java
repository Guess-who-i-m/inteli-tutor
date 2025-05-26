package com.web.amazingtutor.controller;

import com.web.amazingtutor.common.R;
import com.web.amazingtutor.utils.OssUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        //把文件内容存在本地磁盘上
        String originalFilename = file.getOriginalFilename();
        //保证文件名字唯一防止覆盖
        String filename = null;
        if (originalFilename != null) {
            filename = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String url = null;
        try {
            url = OssUtil.uploadFile(filename, file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return R.success(url);
    }
}
