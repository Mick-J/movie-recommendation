package com.mick.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class RecommendationServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }

}
