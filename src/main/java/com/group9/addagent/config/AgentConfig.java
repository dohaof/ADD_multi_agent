package com.group9.addagent.config;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.group9.addagent.model.KnowledgeBase;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares the three collaborating {@link ReactAgent}s that make up the multi-agent team.
 * <p>
 * Each agent is a distinct role (Architect, Quality, Reviewer) carrying its own system
 * instruction derived only from {@link KnowledgeBase}. They are wired into a graph by
 * {@code ADDGraphFactory} and invoked once per ADD iteration.
 */
@Configuration
public class AgentConfig {

    @Bean
    public ReactAgent architectAgent(ChatModel chatModel) throws GraphStateException {
        return ReactAgent.builder()
                .name("architect_agent")
                .model(chatModel)
                .instruction(KnowledgeBase.ARCHITECT_INSTRUCTION)
                .build();
    }

    @Bean
    public ReactAgent qualityAgent(ChatModel chatModel) throws GraphStateException {
        return ReactAgent.builder()
                .name("quality_agent")
                .model(chatModel)
                .instruction(KnowledgeBase.QUALITY_INSTRUCTION)
                .build();
    }

    @Bean
    public ReactAgent reviewerAgent(ChatModel chatModel) throws GraphStateException {
        return ReactAgent.builder()
                .name("reviewer_agent")
                .model(chatModel)
                .instruction(KnowledgeBase.REVIEWER_INSTRUCTION)
                .build();
    }
}
