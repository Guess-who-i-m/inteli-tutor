package com.web.amazingtutor.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;


@Configuration
public class ChatConfig {

    private static final Dotenv dotenv = Dotenv.configure().load();

    @Bean
    public ChatClient chatClient() {
        // 直接在代码中设置 API Key
        String apiKey = dotenv.get("DASHSCOPE_KEY");

        OpenAiApi openAiApi = OpenAiApi.builder()
            .baseUrl("https://dashscope.aliyuncs.com/compatible-mode")
            .apiKey(apiKey)
            .build();

        OpenAiChatOptions options = OpenAiChatOptions.builder()
            .model("deepseek-v3")
            .temperature(0.7)
            .build();

        OpenAiChatModel openAiChatModel = new OpenAiChatModel(
            openAiApi,
            options,
            ToolCallingManager.builder().build(),
            RetryTemplate.defaultInstance(),
            ObservationRegistry.create()
            );

        return ChatClient.builder(openAiChatModel)  // 传入具体模型实例
            .defaultSystem(
                "你是一个擅长数据分析、匹配和推荐的家教中介。" +
                "你的一切回答应当保持口语化，并形成自然段落。" +
                "禁止使用Markdown风格作答。"
            )  // 全局默认系统提示词
            .build();
    }
}
