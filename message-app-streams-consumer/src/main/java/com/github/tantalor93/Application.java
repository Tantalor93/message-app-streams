package com.github.tantalor93;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @KafkaListener(topics = "processed-test")
    public void listen(Long message) {
        System.out.println("Received processed message: " + message);
    }
}
