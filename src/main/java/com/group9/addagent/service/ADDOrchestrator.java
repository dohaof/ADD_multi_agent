package com.group9.addagent.service;

import com.group9.addagent.agent.ArchitectAgent;
import com.group9.addagent.agent.QualityAgent;
import com.group9.addagent.agent.ReviewerAgent;
import com.group9.addagent.model.KnowledgeBase;
import com.group9.addagent.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ADDOrchestrator {

    private final ArchitectAgent architectAgent;
    private final QualityAgent qualityAgent;
    private final ReviewerAgent reviewerAgent;
    private final List<Message> conversationLog = new ArrayList<>();

    public void executeADDProcess() {
        log.info("Starting ADD 3.0 Multi-Agent Process");

        for (int iteration = 1; iteration <= 4; iteration++) {
            log.info("=== Iteration {} ===", iteration);
            executeIteration(iteration);
        }

        saveConversationLog();
        log.info("ADD Process completed. Check conversation_log.txt for full transcript.");
    }

    private void executeIteration(int iteration) {
        String iterationGoal = KnowledgeBase.getIterationGoal(iteration);

        logMessage("SYSTEM", "Starting " + iterationGoal);

        String context = buildContext(iteration);

        logMessage("SYSTEM", "Sending context to Architect Agent");
        String architectProposal = architectAgent.analyze(context);
        logMessage("ARCHITECT", architectProposal);

        logMessage("SYSTEM", "Sending proposal to Quality Agent");
        String qualityEvaluation = qualityAgent.evaluate(architectProposal);
        logMessage("QUALITY", qualityEvaluation);

        logMessage("SYSTEM", "Sending to Reviewer Agent for final decision");
        String finalDesign = reviewerAgent.review(architectProposal, qualityEvaluation);
        logMessage("REVIEWER", finalDesign);

        logMessage("SYSTEM", "Iteration " + iteration + " completed\n" + "=".repeat(80) + "\n");
    }

    private String buildContext(int iteration) {
        return String.format("""
                %s

                %s

                Current Iteration Goal: %s

                Instructions:
                - Follow ADD method steps 1-7
                - Use only provided knowledge
                - Generate views in Mermaid syntax
                - Document all design decisions with rationale
                """,
                KnowledgeBase.ADD_METHOD,
                KnowledgeBase.CASE_STUDY,
                KnowledgeBase.getIterationGoal(iteration)
        );
    }

    private void logMessage(String role, String content) {
        long timestamp = System.currentTimeMillis();
        Message msg = new Message(role, content, timestamp);
        conversationLog.add(msg);

        log.info("\n[{}] {} - {}\n{}\n",
                role,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                timestamp,
                content);
    }

    private void saveConversationLog() {
        try (FileWriter writer = new FileWriter("conversation_log.txt")) {
            writer.write("ADD 3.0 Multi-Agent Conversation Log\n");
            writer.write("Generated: " + LocalDateTime.now() + "\n");
            writer.write("=".repeat(80) + "\n\n");

            for (Message msg : conversationLog) {
                writer.write(String.format("[%s] %s (Timestamp: %d)\n%s\n\n%s\n\n",
                        msg.getRole(),
                        LocalDateTime.ofEpochSecond(msg.getTimestamp() / 1000, 0,
                                java.time.ZoneOffset.UTC),
                        msg.getTimestamp(),
                        "-".repeat(80),
                        msg.getContent()));
            }
        } catch (IOException e) {
            log.error("Failed to save conversation log", e);
        }
    }
}
