# ADD 3.0 Multi-Agent System

## Overview
Multi-agent system implementing ADD 3.0 method for Hotel Pricing System architectural design.

## Architecture
- **Architect Agent**: Proposes design concepts and patterns
- **Quality Agent**: Evaluates designs against quality attributes
- **Reviewer Agent**: Synthesizes and makes final decisions
- **Orchestrator**: Coordinates agent workflow

## Workflow
1. Orchestrator sends context to Architect Agent
2. Architect proposes design
3. Quality Agent evaluates proposal
4. Reviewer synthesizes and produces final design
5. Process repeats for 4 iterations

## Setup
1. Set environment variable: `DASHSCOPE_API_KEY=your_api_key`
2. Run: `mvn spring-boot:run`

## Output
- Console: Real-time agent interactions
- `conversation_log.txt`: Complete conversation with timestamps

## Requirements Met
- Distributed reasoning across 3 specialized agents
- Collaborative verification (Quality + Reviewer)
- Mermaid diagram generation
- No external knowledge beyond provided case study
- All decisions derived from system instructions
