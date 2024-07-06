package org.example.file_extension_blocker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.file_extension_blocker.dto.FileDTO;
import org.example.file_extension_blocker.model.File;
import org.example.file_extension_blocker.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileRestController {
    private final FileService fileService;

    @GetMapping
    public List<FileDTO> getFiles() {
        return fileService.getAllFiles();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일 등록 후 다시 시도해주세요");
        }

        String fileName = file.getOriginalFilename();
        FileDTO fileDTO = FileDTO.builder().name(fileName).build();

        try {
            fileService.addFile(fileDTO);
            return ResponseEntity.ok().body("파일 업로드 성공: " + fileName);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        FileDTO fileDTO = FileDTO.builder().id(id).build();
        try {
            fileService.deleteFile(fileDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usage")
    public ResponseEntity<Boolean> checkExtensionUsage(@RequestParam String extension) {
        boolean isUsed = fileService.isExtensionUsed(extension);
        return ResponseEntity.ok(isUsed);
    }
}