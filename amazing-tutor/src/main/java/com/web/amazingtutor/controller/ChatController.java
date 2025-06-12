package com.web.amazingtutor.controller;

import com.web.amazingtutor.common.R;
import com.web.amazingtutor.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class ChatController {

    private final ChatClient chatClient;

    @PostMapping("/chat")
    public R<String> chat(String prompt) {

        String response = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content();
        return R.success(response);
    }

    @PostMapping("/streamOrigin")
    public Flux<String> streamOrigin(@RequestBody String prompt) {

        Flux<String> response = chatClient
            .prompt()
            .user(prompt)
            .stream()
            .content();

        return response;
    }

    @PostMapping("/stream")
    public Flux<String> stream(@RequestBody List<ChatMessage> messages) {
        // 1. 将 DTO 列表转换为 Spring AI 的 Message 列表
        List<Message> history = messages.stream()
            .map(dto -> {
                switch (dto.getRole().toLowerCase()) {
                    case "user":
                        return new UserMessage(dto.getContent());
                    case "assistant":
                        return new AssistantMessage(dto.getContent());
                    // 你也可以在这里处理 "system" 角色
                    // case "system":
                    //     return new SystemMessage(dto.getContent());
                    default:
                        // 最好抛出异常或记录一个警告
                        return new UserMessage(dto.getContent()); // 默认作为用户消息
                }
            })
            .collect(Collectors.toList());

        // 2. 使用包含历史记录的 Prompt 对象来调用 ChatClient

        String prompt = history.toString();


        // 3. 发起流式请求并返回内容
        return chatClient
            .prompt()
            .user(prompt)
            .stream()
            .content();
    }
}
