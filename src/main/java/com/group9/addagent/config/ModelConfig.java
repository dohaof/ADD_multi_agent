package com.group9.addagent.config;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds the {@link OpenAiChatModel} that all agents share.
 * <p>
 * The model is created manually (rather than via Spring Boot autoconfiguration) so it can
 * point at the PPIO OpenAI-compatible endpoint and so it can be injected into the Spring AI
 * Alibaba {@code ReactAgent} builders.
 */
@Configuration
public class ModelConfig {

    @Value("${DASHSCOPE_API_KEY}")
    private String apiKey;

    @Value("${DASHSCOPE_BASE_URL:https://api.ppio.com/openai}")
    private String baseUrl;

    @Value("${add.model:pa/gpt-5.4}")
    private String model;

    @Bean
    public OpenAiChatModel chatModel() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(model)
                        .build())
                .build();
    }
}
