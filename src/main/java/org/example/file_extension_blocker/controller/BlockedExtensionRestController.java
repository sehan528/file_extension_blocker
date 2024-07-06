package org.example.file_extension_blocker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.file_extension_blocker.service.BlockedExtensionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/extensions")
public class BlockedExtensionRestController {
    private final BlockedExtensionService service;

    @PostMapping("/custom")
    public ResponseEntity<?> addCustomExtension(@RequestParam String name) {
        try {
            service.addCustomExtension(name);
            log.info("커스텀 확장자 등록 성공: {}", name);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("커스텀 확장자 등록 실패: {}, 사유: {}", name, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/custom")
    public ResponseEntity<?> deleteCustomExtension(@RequestParam String name) {
        try {
            service.deleteCustomExtension(name);
            log.info("커스텀 확장자 {} 삭제 성공", name);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("커스텀 확장자 {} 삭제 실패", name, e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/fixed")
    public ResponseEntity<?> toggleFixedExtension(@RequestParam String name, @RequestParam boolean checked) {
        try {
            service.toggleFixedExtension(name, checked);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}