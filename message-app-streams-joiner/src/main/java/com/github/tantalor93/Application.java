package com.github.tantalor93;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    private static final String APPLICATION_ID = "message-app-streams-joiner";
    private static final String BOOTSTRAP_SERVERS = "benky-kafka:9092";
    private static final String INPUT_PROCESSED_TOPIC = "processed-test";
    private static final String INPUT_TOPIC = "test";

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

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        final KTable<String, Long> processedTable = streamsBuilder
                .table(INPUT_PROCESSED_TOPIC, Consumed.with(Serdes.String(), Serdes.Long()));

        final KTable<String, String> inputTable = streamsBuilder
                .table(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        inputTable.join(processedTable,
                (inputValue, processValue) -> inputValue + " -- " + processValue)
                .toStream()
                .foreach((key, val) -> logger.info(val));

        final KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);

        kafkaStreams.start();

    }
}
