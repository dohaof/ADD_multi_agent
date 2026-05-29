package com.group9.addagent.graph;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.StateGraph.END;
import static com.alibaba.cloud.ai.graph.StateGraph.START;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * Wires the three {@link ReactAgent}s into a {@link StateGraph} that expresses the
 * collaborative ADD workflow for a single iteration:
 *
 * <pre>
 *   START -> architect -> quality -> reviewer -> END
 * </pre>
 *
 * The agents share an {@code OverAllState}. The orchestrator seeds it with
 * {@code iteration_context}; each node reads what it needs and writes its own output key.
 * This realises "distributed reasoning" (each agent owns a step) and "collaborative
 * verification" (Quality verifies the Architect, Reviewer reconciles both).
 */
@Configuration
public class ADDGraphFactory {

    /** State keys shared between the agent nodes. */
    public static final String ITERATION_CONTEXT = "iteration_context";
    public static final String ARCHITECT_OUTPUT = "architect_output";
    public static final String QUALITY_OUTPUT = "quality_output";
    public static final String REVIEWER_OUTPUT = "reviewer_output";

    @Bean
    public StateGraph addStateGraph(ReactAgent architectAgent,
                                    ReactAgent qualityAgent,
                                    ReactAgent reviewerAgent) throws GraphStateException {

        KeyStrategyFactory keyStrategyFactory = () -> {
            Map<String, KeyStrategy> strategies = new HashMap<>();
            strategies.put(ITERATION_CONTEXT, new ReplaceStrategy());
            strategies.put(ARCHITECT_OUTPUT, new ReplaceStrategy());
            strategies.put(QUALITY_OUTPUT, new ReplaceStrategy());
            strategies.put(REVIEWER_OUTPUT, new ReplaceStrategy());
            return strategies;
        };

        return new StateGraph("add-iteration", keyStrategyFactory)
                .addNode("architect", node_async(state -> {
                    String context = state.value(ITERATION_CONTEXT, "");
                    AssistantMessage reply = architectAgent.call(context);
                    return Map.of(ARCHITECT_OUTPUT, reply.getText());
                }))
                .addNode("quality", node_async(state -> {
                    String context = state.value(ITERATION_CONTEXT, "");
                    String proposal = state.value(ARCHITECT_OUTPUT, "");
                    String prompt = """
                            %s

                            === Architect Agent proposal to verify ===
                            %s
                            """.formatted(context, proposal);
                    AssistantMessage reply = qualityAgent.call(prompt);
                    return Map.of(QUALITY_OUTPUT, reply.getText());
                }))
                .addNode("reviewer", node_async(state -> {
                    String context = state.value(ITERATION_CONTEXT, "");
                    String proposal = state.value(ARCHITECT_OUTPUT, "");
                    String evaluation = state.value(QUALITY_OUTPUT, "");
                    String prompt = """
                            %s

                            === Architect Agent proposal ===
                            %s

                            === Quality Agent verification ===
                            %s
                            """.formatted(context, proposal, evaluation);
                    AssistantMessage reply = reviewerAgent.call(prompt);
                    return Map.of(REVIEWER_OUTPUT, reply.getText());
                }))
                .addEdge(START, "architect")
                .addEdge("architect", "quality")
                .addEdge("quality", "reviewer")
                .addEdge("reviewer", END);
    }

    @Bean
    public com.alibaba.cloud.ai.graph.CompiledGraph addCompiledGraph(StateGraph addStateGraph)
            throws GraphStateException {
        return addStateGraph.compile();
    }
}
