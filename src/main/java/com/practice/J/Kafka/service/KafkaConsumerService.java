package com.practice.J.Kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "consumerGroup")
    public void listenMessageWithKafka(ConsumerRecord<String, String> message) {
        log.info("============ kafka listen : {} ============", message);
    }

}
