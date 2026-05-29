package com.group9.addagent.service;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.group9.addagent.graph.ADDGraphFactory;
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
import java.util.Map;
import java.util.Optional;

/**
 * Drives the ADD 3.0 process across the four prescribed iterations.
 * <p>
 * For each iteration it seeds the shared graph state with the iteration context and runs the
 * compiled multi-agent graph (architect -> quality -> reviewer), then records every agent's
 * output into a timestamped conversation log.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ADDOrchestrator {

    private final CompiledGraph addCompiledGraph;
    private final List<Message> conversationLog = new ArrayList<>();

    public void executeADDProcess() {
        log.info("Starting ADD 3.0 Multi-Agent Process");

        for (int iteration = 1; iteration <= 4; iteration++) {
            log.info("=== Iteration {} ===", iteration);
            executeIteration(iteration);
            // Persist after every iteration so a long run that is interrupted still
            // leaves a usable transcript (the log is a graded deliverable).
            saveConversationLog();
        }

        saveConversationLog();
        log.info("ADD Process completed. Check conversation_log.txt for full transcript.");
    }

    private void executeIteration(int iteration) {
        String iterationGoal = KnowledgeBase.getIterationGoal(iteration);
        logMessage("SYSTEM", "Starting " + iterationGoal);

        String context = buildContext(iteration);

        Optional<OverAllState> result;
        try {
            result = addCompiledGraph.invoke(Map.of(ADDGraphFactory.ITERATION_CONTEXT, context));
        } catch (Exception e) {
            log.error("Iteration {} failed", iteration, e);
            logMessage("SYSTEM", "Iteration " + iteration + " failed: " + e.getMessage());
            return;
        }

        if (result.isEmpty()) {
            logMessage("SYSTEM", "Iteration " + iteration + " produced no state");
            return;
        }

        OverAllState state = result.get();
        logMessage("ARCHITECT", state.value(ADDGraphFactory.ARCHITECT_OUTPUT, "(no output)"));
        logMessage("QUALITY", state.value(ADDGraphFactory.QUALITY_OUTPUT, "(no output)"));
        logMessage("REVIEWER", state.value(ADDGraphFactory.REVIEWER_OUTPUT, "(no output)"));

        logMessage("SYSTEM", "Iteration " + iteration + " completed\n" + "=".repeat(80) + "\n");
    }

    private String buildContext(int iteration) {
        return String.format("""
                %s

                %s

                Current Iteration Goal: %s

                Instructions:
                - Follow the ADD 3.0 method steps relevant to your role.
                - Use only the provided knowledge above.
                - Generate every architectural view as a Mermaid diagram in a ```mermaid code block.
                - Document all design decisions with their rationale and the drivers they satisfy.
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
