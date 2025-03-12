package com.practice.J.Kafka.controller;

import com.practice.J.Kafka.service.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
@Tag(name = "Kafka API", description = "Kafka messaging API")
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    @Operation(summary = "kafkaSend", description = "kafka를 이용하여 메시지를 송신합니다")
    public ResponseEntity<String> sendMessage(
                        @RequestParam("topic") String topic,
                        @RequestParam("message") String message) {

        try {
            log.info("============ kafka topic : {} ============", topic);
            kafkaProducerService.sendMessageWithKafka(topic, message);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            log.error("============ kafka error : {} ============", e.getMessage());
            return ResponseEntity.badRequest().body("fail");
        }

    }
}
