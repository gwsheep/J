package com.practice.J.Exception;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(ExceptionTest.TestController.class)
@Import(ExceptionTest.TestConfig.class)
public class ExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void testCustomExceptionHandling() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("exception occurred"));
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public TestController testController() {
            return new TestController();
        }

        @Bean
        public TestExceptionHandler testExceptionHandler() {
            return new TestExceptionHandler();
        }
    }


    @RestController
    static class TestController {

        @GetMapping("/test")
        public ResponseEntity<String> hello() {
            return ResponseEntity.ok("ok");
        }

        @GetMapping("/error")
        public ResponseEntity<String> error() {
            throw new CustomException("exception occurred");
        }
    }

    @RestControllerAdvice
    static class TestExceptionHandler {

        @ExceptionHandler(CustomException.class)
        public ResponseEntity<String> handleCustomException(CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    static class CustomException extends RuntimeException {
        public CustomException(String message) {
            super(message);
        }
    }

}

