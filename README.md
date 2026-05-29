# ADD 3.0 Multi-Agent System

## Overview
Multi-agent system implementing the ADD 3.0 method for the Hotel Pricing System architectural design, built on **Spring AI Alibaba** (`ReactAgent` + Graph runtime).

## Architecture
Three `ReactAgent`s collaborate, wired as nodes in a Spring AI Alibaba `StateGraph`:
- **Architect Agent**: identifies drivers and proposes design concepts / patterns (ADD steps 1–5)
- **Quality Agent**: verifies the proposal against the quality attributes and constraints (collaborative verification)
- **Reviewer Agent**: synthesizes both into the final views and decision record (ADD steps 6–7)
- **Orchestrator**: invokes the compiled graph once per iteration and logs the transcript

Graph: `START → architect → quality → reviewer → END`, with the agents sharing an `OverAllState`.

## Workflow
For each of the 4 iterations:
1. Orchestrator builds the context (ADD method + case study + iteration goal)
2. Compiled `StateGraph` is invoked with the context
3. Architect → Quality → Reviewer run in sequence, each reading the prior outputs from shared state
4. All three outputs are logged with timestamps
5. The transcript is saved after every iteration

## Setup
1. Put your key in `.env`: `DASHSCOPE_API_KEY=...` and `DASHSCOPE_BASE_URL=https://api.ppio.com/openai`
2. Run: `./run.sh` (or `mvn -s .mvn/settings.xml spring-boot:run`)

> The build uses `-s .mvn/settings.xml` to work around the machine's global Maven mirror, which serves Maven Central over http and is blocked by Maven 3.9+.

## Output
- Console: real-time agent interactions
- `conversation_log.txt`: complete conversation with timestamps (saved incrementally)

## Requirements Met
- Multi-agent paradigm: distributed reasoning across 3 specialized `ReactAgent`s + collaborative verification
- Mermaid diagram generation for all views
- No external knowledge beyond the provided case study; no few-shot examples
- All role instructions and decision rules derived from the system instructions in `KnowledgeBase`
