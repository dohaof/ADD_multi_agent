package com.group9.addagent.agent;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArchitectAgent {

    private final ChatClient.Builder chatClientBuilder;

    public String analyze(String context) {
        return chatClientBuilder.build()
                .prompt()
                .system("""
                        You are an experienced Software Architect specializing in ADD method.
                        Your role is to:
                        1. Review inputs and identify architectural drivers
                        2. Establish iteration goals
                        3. Select system elements to refine
                        4. Propose design concepts and patterns

                        Generate architectural views using Mermaid syntax.
                        Base all decisions on provided knowledge only.
                        """)
                .user(context)
                .call()
                .content();
    }
}
