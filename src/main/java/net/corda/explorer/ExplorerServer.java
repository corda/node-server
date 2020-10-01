package net.corda.explorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class ExplorerServer {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ExplorerServer.class);
        app.run(args);
    }
}