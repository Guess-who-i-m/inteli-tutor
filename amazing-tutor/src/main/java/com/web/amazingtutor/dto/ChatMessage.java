package com.web.amazingtutor.dto;

// ChatMessageDTO.java
// 可以使用 Lombok 来简化代码
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String role;
    private String content;
}