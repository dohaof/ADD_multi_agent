package com.group9.addagent.agent;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QualityAgent {

    private final ChatClient.Builder chatClientBuilder;

    public String evaluate(String design) {
        return chatClientBuilder.build()
                .prompt()
                .system("""
                        You are a Quality Assurance Architect.
                        Your role is to:
                        1. Evaluate designs against quality attributes
                        2. Identify risks and tradeoffs
                        3. Verify quality attribute scenarios are addressed
                        4. Suggest improvements for reliability, performance, security

                        Base evaluations on provided quality attributes only.
                        """)
                .user(design)
                .call()
                .content();
    }
}
