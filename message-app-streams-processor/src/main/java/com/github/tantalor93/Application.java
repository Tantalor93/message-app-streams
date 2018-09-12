package com.github.tantalor93;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String APPLICATION_ID = "message-app-streams-processor";
    private static final String BOOTSTRAP_SERVERS = "benky-kafka:9092";
    private static final String INPUT_TOPIC = "test";
    private static final String OUTPUT_TOPIC = "processed-test";

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(final String... args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        final StreamsBuilder builder = new StreamsBuilder();
        builder.<String, String>stream(INPUT_TOPIC)
                .mapValues(value -> countAlphabeticLetters(value))
                .to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

        final KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        kafkaStreams.start();
    }

    public static Long countAlphabeticLetters(final String value) {
        long count = value.codePoints().filter(codePoint -> Character.isAlphabetic(codePoint)).count();
        logger.info("WORD={} COUNT={}", value, count);
        return count;
    }
}
