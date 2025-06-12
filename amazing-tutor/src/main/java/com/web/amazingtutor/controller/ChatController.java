package com.web.amazingtutor.controller;

import com.web.amazingtutor.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

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

    @PostMapping("/stream")
    public Flux<String> stream(@RequestBody String prompt) {

        Flux<String> response = chatClient
            .prompt()
            .user(prompt)
            .stream()
            .content();

        return response;
    }
}
