package com.practice.J.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import com.practice.J.pb.PBProto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Proto API", description = "ProtoBuf 변환 API")
public class PBProtoController {

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/proto/jsontopb", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "JsonToPb", description = "json을 pb로 변환해줍니다")
    public ResponseEntity<?> getJsonToPb(@RequestPart("file") MultipartFile file) {

        log.info("========== Protobuf .proto -> protoc start ==========");

        try {
            JsonNode jsonNode = objectMapper.readTree(file.getBytes());
            PBProto.Result.Builder fruitBuilder = PBProto.Result.newBuilder();
            JsonFormat.parser().merge(jsonNode.toString(), fruitBuilder);
            PBProto.Result result = fruitBuilder.build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fruit.pb")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(result.toByteArray());

        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("fail to parse");
        }

    }

    @PostMapping(value = "/proto/pbtojson", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "PbToJson", description = "PB을 Json로 변환해줍니다")
    public ResponseEntity<?> getPbToJson(@RequestPart("file") MultipartFile file) {

        log.info("========== Protobuf .proto -> protoc start ==========");

        try {
            byte[] pbFile = file.getBytes();
            PBProto.Result receiveMessage = PBProto.Result.parseFrom(pbFile);
            String message = JsonFormat.printer().print(receiveMessage);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(message);

        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body("failed to parse");
        }

    }


    //proto 파일 작성 및 컴파일 후 테스트용
    @GetMapping(value = "/test/proto", produces = "application/x-protobuf")
    public PBProto.Result protoTest() {
        PBProto.Fruit apple = PBProto.Fruit.newBuilder()
                                                        .setName("Apple")
                                                        .setQuantity(10)
                                                        .build();

        PBProto.Fruit banana = PBProto.Fruit.newBuilder()
                                                        .setName("Banana")
                                                        .setQuantity(20)
                                                        .build();

        return PBProto.Result.newBuilder()
                .addResult(apple)
                .addResult(banana)
                .build();
    }


}
