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

1. **Sequential Flow**: Architect → Quality → Reviewer
2. **Context Passing**: Each agent receives relevant context from previous agents
3. **No Cross-Talk**: Agents do not directly communicate; orchestrator mediates
4. **Single Iteration**: Each agent processes once per iteration

### Workflow

```
For each iteration (1-4):
  1. Orchestrator prepares context (ADD method + Case study + Iteration goal)
  2. Architect Agent receives context and proposes design
  3. Quality Agent receives proposal and evaluates
  4. Reviewer Agent receives both and produces final design
  5. All interactions logged with timestamps
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
- Spring Boot 3.3.0
- Spring AI Alibaba 1.0.0-M3.2
- Java 21
- Maven

### Key Components

1. **ADDOrchestrator.java**: Coordinates multi-agent workflow
2. **ArchitectAgent.java**: Proposes designs
3. **QualityAgent.java**: Evaluates quality
4. **ReviewerAgent.java**: Makes final decisions
5. **KnowledgeBase.java**: Stores all prior knowledge
6. **Message.java**: Conversation log model

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

- System uses deepseek-v4-pro model via DashScope API
- All agent prompts are system-level instructions
- No hardcoded architectural decisions
- Agents collaborate through orchestrator mediation
