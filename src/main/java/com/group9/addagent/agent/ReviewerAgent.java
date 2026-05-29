package com.group9.addagent.agent;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewerAgent {

    private final ChatClient.Builder chatClientBuilder;

    public String review(String proposal, String evaluation) {
        return chatClientBuilder.build()
                .prompt()
                .system("""
                        You are a Design Reviewer.
                        Your role is to:
                        1. Synthesize architect proposals and quality evaluations
                        2. Make final design decisions
                        3. Document design rationale
                        4. Ensure completeness of iteration goals

                        Produce final architectural views in Mermaid syntax.
                        Record all design decisions with rationale.
                        """)
                .user("Proposal:\n" + proposal + "\n\nEvaluation:\n" + evaluation)
                .call()
                .content();
    }
}
