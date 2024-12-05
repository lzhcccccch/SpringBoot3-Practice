package cn.lzhch.tika.controller;


import cn.lzhch.tika.dto.DocumentInfo;
import cn.lzhch.tika.service.IDocumentParserService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * 文档解析控制器
 * <p>
 * author: lzhch
 * version: v1.0
 * date: 2024/12/3 17:44
 */

@Slf4j
@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    @Resource
    private final IDocumentParserService documentParserService;

    @PostMapping("/parse")
    public ResponseEntity<DocumentInfo> parseDocument(@RequestParam("file") MultipartFile file) {
        try {
            DocumentInfo documentInfo = documentParserService.parseDocument(file);
            return ResponseEntity.ok(documentInfo);
        } catch (TikaException | IOException e) {
            log.error("Error parsing document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/detect-type")
    public ResponseEntity<Map<String, String>> detectFileType(@RequestParam("file") MultipartFile file) {
        try {
            String mimeType = documentParserService.detectMimeType(file);
            return ResponseEntity.ok(Collections.singletonMap("mimeType", mimeType));
        } catch (IOException e) {
            log.error("Error detecting file type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
        try (InputStream input = file.getInputStream()) {
            String text = documentParserService.extractText(input);
            return ResponseEntity.ok(text);
        } catch (TikaException | IOException e) {
            log.error("Error extracting text", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
