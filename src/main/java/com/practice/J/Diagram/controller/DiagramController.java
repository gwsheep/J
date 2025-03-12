package com.practice.J.Diagram.controller;

import com.practice.J.Diagram.service.DiagramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/diagram")
@Tag(name = "Diagram API", description = "Diagram 생성 API")
public class DiagramController {

    private final DiagramService diagramService;

    //javascript 느낌..
    @GetMapping(value = "/get")
    @Operation(summary = "Make Diagram", description = "도형을 생성해줍니다")
    public ResponseEntity<?> getDiagram() {

        log.info("========== Make Diagram start ==========");
        diagramService.getDiagram();
        return ResponseEntity.ok().body("ok");

    }

}
