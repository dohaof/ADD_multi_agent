package com.group9.addagent.model;

public class KnowledgeBase {

    public static final String ADD_METHOD = """
            # Attribute-Driven Design (ADD) Method:

            ## Step 1 Review Inputs
            Review the inputs and identify which requirements will be considered as architectural drivers.

            ## Step 2 Establish the Iteration Goal by Selecting Drivers
            A design round generally takes the form of a series of design iterations, where each iteration focuses on achieving a particular goal.

            ## Step 3 Choose One or More Elements of the System to Refine
            This step is where the core design activities start. The elements that you will select are the ones that are involved in the satisfaction of specific drivers.

            ## Step 4 Choose One or More Design Concepts That Satisfy the Selected Drivers
            This step requires you to identify alternatives among design concepts that can be used to achieve your iteration goal, and to select one of these alternatives.

            ## Step 5 Instantiate Architectural Elements, Allocate Responsibilities, and Define Interfaces
            This step requires instantiating architectural elements based on the selected design concepts and assigning responsibilities to them.

            ## Step 6 Sketch Views and Record Design Decisions
            At this point, you have completed the design activities for the iteration. Preserve the views and record the significant decisions made during the design iteration.

            ## Step 7 Perform Analysis of Current Design and Review Iteration Goal and Achievement of Design Purpose
            This step checks whether a partial design that satisfies the goals of the current iteration has been created, and considers whether additional design iterations are needed.
            """;

    public static final String CASE_STUDY = """
            # Use ADD to design a greenfield system in a mature domain: Hotel Pricing System
            ##Design purpose: This project can be considered greenfield development, as it involves the complete replacement of
            an existing system. The purpose of the design activity is to make initial decisions to support the construction of the
            system from scratch.
            ##Primary Functionality:
            |Use Case| Description|
            | ---- | ---- |
            |HPS-1: Log In| A user (commercial or administrator) provides their credentials in a login window. The system checks
            these credentials against a user identity service and, if successful, provides access to the system. Once logged in, a user
            can only make queries and changes to the hotels for which they have been authorized. |
            |HPS-2: Change Prices| A user selects a specific hotel for which they are authorized to change prices, and selects dates
            where they want to make price changes to either a base rate or a fixed rate. All of the prices for the rates that are
            calculated from the base rate are calculated at that point. The system allows price changes to be simulated before they
            are actually changed. When the prices are changed, they are pushed to the Channel Management System and become
            available for querying by external systems. |
            |HPS-3: Query Prices| A user or an external system queries prices for a given hotel through the user interface or a
            query API. |
            |HPS-4: Manage Hotels| An administrator adds, changes, or modifies hotel information. This includes editing the
            hotel’s tax rates, available rates, and room types. |
            |HPS-5: Manage Rates| An administrator adds, changes, or modifies rates. This includes defining the calculation
            business rules for the different rates. |
            |HPS-6: Manage Users| An administrator changes permissions for a given user. |
            ##Quality Attributes:
            |ID |Quality Attribute |Scenario |Associated Use Case| Importance to the Customer |Difficulty of Implementation |
            | ---- | ---- | ---- |---- | ---- |---- |
            |QA-1|Performance|A base rate price is changed for a specific hotel and date during normal operation; the prices for
            all the rates and room types for the hotel are published (ready for query) in less than 100 ms. |HPS-2|High|High|
            |QA-2|Reliability|A user performs multiple price changes on a given hotel; 100% of the price changes are published
            (available for query) successfully and are also received by the Channel Management System.|HPS-2| High|High|
            |QA-3|Availability|Pricing queries uptime SLA must be 99.9% outside of maintenance windows.|All| High|High|
            |QA-4|Scalability| The system will initially support a minimum of 100,000 price queries per day through its API and
            should be capable of handling up to 1,000,000 without decreasing average latency by more than 20%.|HPS-3|
            High|High|
            |QA-5|Security|A user logs into the system through the front-end. The credentials of the user are validated against the
            User Identity Service and, once logged in, they are presented with only the functions that they are authorized to use.|All|
            High| Medium|
            |QA-6|Modifiability|Support for a price query endpoint with a different protocol than REST (e.g., gRPC) is added to
            the system. The new endpoint does not require changes to be made to the core components of the system.|All|Medium|
            Medium|
            |QA-7| |Deployability|The application is moved between nonproduction environments as part of the development
            process. No changes in the code are needed.|All | Medium| Medium|
            |QA-8| |Monitorability| A system operator wishes to measure the performance and reliability of price publication
            during operation. The system provides a mechanism that allows 100% of these measures to be collected as needed.|
            HPS-2| Medium| Medium|
            |QA-9|Testability|100% of the system and its elements should support integration testing independently of the external
            systems.| All| Medium| Medium|
            ##Architectural Concerns:
            |ID |Concern|
            |CRN-1| Establish an overall initial system structure. |
            |CRN-2| Leverage the team’s knowledge about Java technologies, the Angular framework, and Kafka. |
            |CRN-3| Allocate work to members of the development team. |
            |CRN-4| Avoid introducing technical debt.|
            |CRN-5| Set up a continuous deployment infrastructure.|
            ##Constraints:
            |ID |Constraint|
            |CON-1 |Users must interact with the system through a web browser
            in different platforms (Windows, OSX, and Linux, and different devices).|
            |CON-2 | Manage users through cloud provider identity service and host resources in the cloud.|
            |CON-3 | Code must be hosted on a proprietary Git-based platform that is already in use by other projects in the
            company. |
            |CON-4 | The initial release of the system must be delivered in 6 months, but an initial version of the system (MVP)
            must be demonstrated to internal stakeholders in at most 2 months. |
            |CON-5 | The system must interact initially with existing systems through REST APIs but may need to later support
            other protocols. |
            |CON-6 | A cloud-native approach should be favored when designing the system.|
            """;

    // ===================== Multiple Role Prompts (system instructions) =====================
    // Each role instruction is derived ONLY from the prior knowledge above (ADD_METHOD,
    // CASE_STUDY) and from the assignment requirements. No external knowledge, no few-shot
    // examples, and no handcrafted demonstration outputs are included.

    public static final String ARCHITECT_INSTRUCTION = """
            You are the Architect Agent in a multi-agent team applying the ADD 3.0 method.

            Your responsibility for the current iteration:
            - ADD Step 1: Review the inputs and identify which requirements (functional
              requirements HPS-*, quality attributes QA-*, concerns CRN-*, constraints CON-*)
              are the architectural drivers for THIS iteration's goal.
            - ADD Step 2: Restate the iteration goal in terms of the drivers you selected.
            - ADD Step 3: Choose which element(s) of the system to refine.
            - ADD Step 4: Identify candidate design concepts (patterns, reference
              architectures, tactics) that satisfy the selected drivers, compare the
              alternatives, and select one. Justify the selection using ONLY the provided
              drivers and constraints.
            - ADD Step 5: Instantiate architectural elements, allocate responsibilities, and
              define interfaces.

            Dialogue rules:
            - You produce the initial design proposal that the Quality Agent will verify and
              the Reviewer Agent will finalise. Make your drivers, decisions, and rationale
              explicit so they can be checked.
            - Use ONLY the provided prior knowledge. Do not invent requirements, technologies,
              or facts that are not present in the case study.
            - Provide every architectural view as a Mermaid diagram inside a ```mermaid fenced
              code block.
            """;

    public static final String QUALITY_INSTRUCTION = """
            You are the Quality Agent in a multi-agent team applying the ADD 3.0 method. You
            perform collaborative verification of the Architect Agent's proposal.

            Your responsibility for the current iteration (supports ADD Step 7 analysis):
            - Verify that the Architect selected the correct architectural drivers for the
              iteration goal, and flag any missing or irrelevant drivers.
            - Evaluate the proposed design concepts and element responsibilities against the
              relevant quality attributes (QA-1 Performance, QA-2 Reliability, QA-3
              Availability, QA-4 Scalability, QA-5 Security, QA-6 Modifiability, QA-7
              Deployability, QA-8 Monitorability, QA-9 Testability) and the constraints CON-*.
            - Identify risks, tradeoffs, and any quality-attribute scenario that is not yet
              addressed. Recommend concrete, verifiable improvements.

            Dialogue rules:
            - Do NOT redesign the system. Critique and verify the Architect's proposal so the
              Reviewer can make an informed final decision.
            - Base every judgement ONLY on the provided prior knowledge. Do not introduce
              external standards, products, or benchmarks.
            - If you reference or correct a view, express it with a Mermaid diagram inside a
              ```mermaid fenced code block.
            """;

    public static final String REVIEWER_INSTRUCTION = """
            You are the Reviewer Agent in a multi-agent team applying the ADD 3.0 method. You
            synthesise the Architect proposal and the Quality verification into the final
            design for the iteration.

            Your responsibility for the current iteration:
            - ADD Step 6: Produce the final architectural views and record the significant
              design decisions made during the iteration, each with its rationale and the
              drivers it satisfies.
            - ADD Step 7: Analyse the current design, state whether the iteration goal has
              been achieved, list any remaining drivers or risks, and note what later
              iterations must still address.

            Dialogue rules:
            - Reconcile the Architect proposal with the Quality Agent's findings. Where they
              conflict, decide explicitly and explain why, using ONLY the provided drivers and
              constraints.
            - Use ONLY the provided prior knowledge. Do not add requirements or technologies
              that are not in the case study.
            - Present every final architectural view as a Mermaid diagram inside a ```mermaid
              fenced code block, and provide a decision table (decision, rationale, drivers
              addressed) for the iteration.
            """;

    public static String getIterationGoal(int iteration) {
        return switch (iteration) {
            case 1 -> "Iteration 1: Establishing an Overall System Structure";
            case 2 -> "Iteration 2: Identifying Structures to Support Primary Functionality";
            case 3 -> "Iteration 3: Addressing Reliability and Availability Quality Attributes";
            case 4 -> "Iteration 4: Addressing Development and Operations";
            default -> throw new IllegalArgumentException("Invalid iteration: " + iteration);
        };
    }
}
