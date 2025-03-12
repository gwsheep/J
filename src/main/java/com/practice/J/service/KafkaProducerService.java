package com.practice.J.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessageWithKafka(String topic, String message) {

        kafkaTemplate.send(topic, message);
        log.info("============ kafka send : {} ============", message);

    }

}
