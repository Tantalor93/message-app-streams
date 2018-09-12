package com.github.tantalor93;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @KafkaListener(topics = "processed-test")
    public void listen(final ConsumerRecord<String, Long> record) {
        logger.info(
                "Received record with key={} and value={}",
                record.key(),
                record.value()
        );
    }
}
