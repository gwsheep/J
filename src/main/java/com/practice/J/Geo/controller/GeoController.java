package com.practice.J.Geo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.J.Geo.dto.Geo;
import com.practice.J.Geo.service.GeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/geo")
@Tag(name = "Geographical Item API", description = "Geographical Item 생성 API")
public class GeoController {

    private final ObjectMapper objectMapper;
    private final GeoService geoService;

    @GetMapping("/point")
    @Operation(summary = "Make Point Item", description = "좌표 예시를 생성해줍니다")
    public ResponseEntity<?> getPoint() {

        log.info("========== Make Point Item start ==========");
        try {
            String point = geoService.getPointItem();
            String jsonPoint = this.getJson(new Geo(), "Point", point);
            return ResponseEntity.ok().body(jsonPoint);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/line")
    @Operation(summary = "Make Line Item", description = "선분(line) 예시를 생성해줍니다")
    public ResponseEntity<?> getLine() {

        log.info("========== Make Line Item start ==========");
        try {
            String line = geoService.getLineItem();
            String jsonLine = this.getJson(new Geo(), "Line", line);
            return ResponseEntity.ok().body(jsonLine);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    public String getJson(Geo geo, String type, String geographic) throws JsonProcessingException {
        geo.setGeoType(type);
        geo.setExample(geographic);
        return objectMapper.writeValueAsString(geo);
    }

}
