package com.practice.J.Protobuf.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import com.practice.J.Protobuf.pb.PBDynamicDescriptor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/dynamic")
@Tag(name = "Proto - DynamicMessage API", description = "ProtoBuf 동적 변환 API")
public class PBDynamicController {

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/jsontopb", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "JsonToPb", description = "json을 pb로 변환해줍니다")
    public ResponseEntity<?> getJsonToPb(@RequestPart("file") MultipartFile file) {

        log.info("========== Protobuf Dynamic Message start ==========");

        try {
            JsonNode jsonNode = objectMapper.readTree(file.getBytes());
            Descriptors.Descriptor descriptor = PBDynamicDescriptor.getDescriptor();
            DynamicMessage.Builder builder = DynamicMessage.newBuilder(descriptor);
            JsonFormat.parser().merge(jsonNode.toString(), builder);
            DynamicMessage dynamicMessage = builder.build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fruit.pb")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(dynamicMessage.toByteArray());

        } catch (IOException | Descriptors.DescriptorValidationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("fail to parse");
        }
    }

    @PostMapping(value = "/pbtojson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "PbToJson", description = "PB을 Json로 변환해줍니다")
    public ResponseEntity<?> getPbToJson(@RequestPart("file") MultipartFile file) {

        log.info("========== Protobuf Dynamic Message start ==========");

        try {
            byte[] pbFile = file.getBytes();
            Descriptors.Descriptor descriptor = PBDynamicDescriptor.getDescriptor();
            DynamicMessage dynamicMessage = DynamicMessage.parseFrom(descriptor, pbFile);
            String message = JsonFormat.printer().print(dynamicMessage);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message);

        } catch (IOException | Descriptors.DescriptorValidationException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("failed to parse");
        }
    }

}
