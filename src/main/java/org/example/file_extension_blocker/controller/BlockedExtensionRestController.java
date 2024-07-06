package org.example.file_extension_blocker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.service.BlockedExtensionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/extensions")
public class BlockedExtensionRestController {
    private final BlockedExtensionService service;

    @PostMapping("/fixed")
    public ResponseEntity<?> toggleFixedExtension(@RequestBody BlockedExtensionDTO dto) {
        try {
            service.toggleFixedExtension(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/custom")
    public ResponseEntity<?> addCustomExtension(@RequestBody BlockedExtensionDTO dto) {
        try {
            service.addCustomExtension(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/custom")
    public ResponseEntity<?> deleteCustomExtension(@RequestBody BlockedExtensionDTO dto) {
        try {
            service.deleteCustomExtension(dto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}