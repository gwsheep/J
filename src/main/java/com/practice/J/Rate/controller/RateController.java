package com.practice.J.Rate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Rate Limiter - Bucket4J + Redis API", description = "Bucket4J + Redis Test API")
@RequestMapping("/rate")
public class RateController {

    @Operation(summary = "test1")
    @GetMapping("/test")
    public ResponseEntity<?> getTestRate() {
        /* 응답 interval 두기 */
        log.info("==== test_1 API 호출");

        try {
            Thread.sleep(1000);
            return ResponseEntity.ok("test 요청");
        } catch (InterruptedException e) {
            log.error("에러 발생 = " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

//        return ResponseEntity.ok("test response ok");
    }

    @Operation(summary = "test2")
    @GetMapping("/test2")
    public ResponseEntity<?> getTestRate2() {
        log.info("==== test_2 API 호출");

        try {
            Thread.sleep(500);
            return ResponseEntity.ok("test 요청");
        } catch (InterruptedException e) {
            log.error("에러 발생 = " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

//        return ResponseEntity.ok("test response ok");
    }

    @Operation(summary = "test3")
    @GetMapping("/test3")
    public ResponseEntity<?> getTestRate3() {
        log.info("==== test_3 API 호출");

        try {
            Thread.sleep(2000);
            return ResponseEntity.ok("test 요청");
        } catch (InterruptedException e) {
            log.error("에러 발생 = " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

//        return ResponseEntity.ok("test response ok");
    }

}