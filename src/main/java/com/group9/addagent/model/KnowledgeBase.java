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
            # Hotel Pricing System Case Study

            ## Design Purpose
            This project can be considered greenfield development, as it involves the complete replacement of an existing system.

            ## Primary Functionality
            - HPS-1: Log In - A user provides credentials, system validates against user identity service
            - HPS-2: Change Prices - User changes base/fixed rates for authorized hotels, prices calculated and pushed to Channel Management System
            - HPS-3: Query Prices - User or external system queries prices via UI or API
            - HPS-4: Manage Hotels - Administrator manages hotel information, tax rates, room types
            - HPS-5: Manage Rates - Administrator manages rates and calculation business rules
            - HPS-6: Manage Users - Administrator changes user permissions

            ## Quality Attributes
            - QA-1 Performance: Price changes published in <100ms
            - QA-2 Reliability: 100% price changes published successfully
            - QA-3 Availability: 99.9% uptime SLA
            - QA-4 Scalability: Support 100K-1M queries/day
            - QA-5 Security: Credential validation and authorization
            - QA-6 Modifiability: Support different protocols without core changes
            - QA-7 Deployability: Move between environments without code changes
            - QA-8 Monitorability: 100% performance/reliability measures collection
            - QA-9 Testability: 100% integration testing support

            ## Architectural Concerns
            - CRN-1: Establish overall initial system structure
            - CRN-2: Leverage Java, Angular, Kafka knowledge
            - CRN-3: Allocate work to development team
            - CRN-4: Avoid technical debt
            - CRN-5: Set up continuous deployment infrastructure

            ## Constraints
            - CON-1: Web browser access across platforms
            - CON-2: Cloud provider identity service and hosting
            - CON-3: Git-based platform for code hosting
            - CON-4: 6 months delivery, 2 months MVP
            - CON-5: REST APIs initially, support other protocols later
            - CON-6: Cloud-native approach
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
