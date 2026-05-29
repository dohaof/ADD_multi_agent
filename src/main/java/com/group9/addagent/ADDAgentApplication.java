package com.group9.addagent;

import com.group9.addagent.service.ADDOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ADDAgentApplication implements CommandLineRunner {

    private final ADDOrchestrator orchestrator;

    public static void main(String[] args) {
        SpringApplication.run(ADDAgentApplication.class, args);
    }

    @Override
    public void run(String... args) {
        orchestrator.executeADDProcess();
    }
}
