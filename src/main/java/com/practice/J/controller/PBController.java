package com.practice.J.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.util.JsonFormat;
import com.practice.J.pb.PBSample;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PBController {

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/jsontopb", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> getJsonToPb(@RequestPart("file") MultipartFile file) {
        try {
            JsonNode jsonNode = objectMapper.readTree(file.getBytes());
            Descriptors.Descriptor descriptor = PBSample.getDescriptor();
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
    public ResponseEntity<?> getPbToJson(@RequestPart("file") MultipartFile file) {

        try {
            byte[] pbFile = file.getBytes();
            Descriptors.Descriptor descriptor = PBSample.getDescriptor();
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
