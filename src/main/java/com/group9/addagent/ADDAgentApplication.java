package com.group9.addagent;

import com.group9.addagent.service.ADDOrchestrator;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ADDAgentApplication implements CommandLineRunner {

    private final ADDOrchestrator orchestrator;

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
        SpringApplication.run(ADDAgentApplication.class, args);
    }

    @Override
    public void run(String... args) {
        orchestrator.executeADDProcess();
    }
}
