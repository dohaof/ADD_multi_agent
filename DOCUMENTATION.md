# ADD 3.0 Multi-Agent System Documentation

## System Architecture

### Agent Roles

#### 1. Architect Agent
**Responsibility**: Propose architectural designs following ADD method
**System Instructions**:
- Review inputs and identify architectural drivers
- Establish iteration goals
- Select system elements to refine
- Propose design concepts and patterns
- Generate architectural views using Mermaid syntax
- Base all decisions on provided knowledge only

#### 2. Quality Agent
**Responsibility**: Evaluate designs against quality attributes
**System Instructions**:
- Evaluate designs against quality attributes (QA-1 to QA-9)
- Identify risks and tradeoffs
- Verify quality attribute scenarios are addressed
- Suggest improvements for reliability, performance, security
- Base evaluations on provided quality attributes only

#### 3. Reviewer Agent
**Responsibility**: Synthesize proposals and make final decisions
**System Instructions**:
- Synthesize architect proposals and quality evaluations
- Make final design decisions
- Document design rationale
- Ensure completeness of iteration goals
- Produce final architectural views in Mermaid syntax
- Record all design decisions with rationale

### Dialogue Rules

1. **Sequential Flow**: Architect → Quality → Reviewer, implemented as a Spring AI Alibaba `StateGraph` (`START → architect → quality → reviewer → END`).
2. **Context Passing**: Agents share an `OverAllState`. The Architect reads the iteration context; the Quality agent reads the context + Architect output; the Reviewer reads the context + both prior outputs.
3. **No Cross-Talk**: Agents do not call each other directly; the graph edges and shared state mediate all data flow.
4. **Single Iteration**: The graph is invoked once per iteration; each agent runs once per invocation.

### Workflow

```
For each iteration (1-4):
  1. Orchestrator builds context (ADD method + Case study + Iteration goal)
  2. Orchestrator invokes the compiled StateGraph with iteration_context
  3. architect node  -> ReactAgent(architect) -> writes architect_output
  4. quality node    -> ReactAgent(quality)   -> reads architect_output, writes quality_output
  5. reviewer node   -> ReactAgent(reviewer)  -> reads both, writes reviewer_output
  6. Orchestrator reads all three outputs from final state and logs them with timestamps
```

## Prior Knowledge Provided

### ADD 3.0 Method
Complete 7-step process embedded in KnowledgeBase.java

### Hotel Pricing System Case Study
- Design purpose
- Primary functionality (HPS-1 to HPS-6)
- Quality attributes (QA-1 to QA-9)
- Architectural concerns (CRN-1 to CRN-5)
- Constraints (CON-1 to CON-6)

### Iteration Plan
- Iteration 1: Establishing an Overall System Structure
- Iteration 2: Identifying Structures to Support Primary Functionality
- Iteration 3: Addressing Reliability and Availability Quality Attributes
- Iteration 4: Addressing Development and Operations

## Implementation Details

### Technology Stack
- Spring Boot 3.5.8
- Spring AI Alibaba Agent Framework 1.1.2.2 (ReactAgent + Graph runtime)
- Spring AI 1.1.2 (OpenAI-compatible ChatModel)
- Java 21
- Maven

### Key Components

1. **ModelConfig.java**: Builds the shared `OpenAiChatModel` pointing at the PPIO endpoint.
2. **AgentConfig.java**: Declares the three `ReactAgent` beans (architect / quality / reviewer), each with its role instruction.
3. **ADDGraphFactory.java**: Wires the three agents into a `StateGraph` and compiles it.
4. **ADDOrchestrator.java**: Invokes the compiled graph once per iteration and logs outputs.
5. **KnowledgeBase.java**: Stores all prior knowledge and the three role instructions.
6. **Message.java**: Conversation log model.

### Conversation Logging
- All agent interactions logged with timestamps
- Saved to `conversation_log.txt`
- Includes role, timestamp, and full content

## Compliance with Requirements

✅ Views generated using Mermaid syntax
✅ No external domain knowledge beyond provided prior knowledge
✅ No few-shot examples or handcrafted demonstrations
✅ No additional task reinterpretation
✅ All decisions derived from system instructions
✅ Agents decompose tasks and verify through collaboration
✅ Multiple role prompts (3 agents)
✅ Defined dialogue rules
✅ Explicit workflow

## Running the System

1. Set API key: `export DASHSCOPE_API_KEY=your_key`
2. Run: `./run.sh` or `mvn spring-boot:run`
3. Check output in console and `conversation_log.txt`

## Expected Output

For each iteration:
- Architect's design proposal with Mermaid diagrams
- Quality evaluation with risk analysis
- Reviewer's final design with rationale
- Complete timestamp trail

## Notes

- The model is `pa/gpt-5.4`, accessed through the PPIO OpenAI-compatible endpoint (`DASHSCOPE_BASE_URL`) using `DASHSCOPE_API_KEY`. Change `add.model` in `application.yml` to switch models.
- All agent prompts are system-level instructions derived only from the prior knowledge.
- No hardcoded architectural decisions.
- Agents collaborate through the StateGraph and shared state, not direct calls.
