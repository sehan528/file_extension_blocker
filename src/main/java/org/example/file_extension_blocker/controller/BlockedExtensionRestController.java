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
            log.info("성공. 커스텀 확장자 {} 삭제", name);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("실패. 커스텀 확장자 {} 삭제", name, e);
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

//    @PostMapping("/upload")
//    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            log.warn("파일 업로드 실패: 파일이 비어있음");
//            return ResponseEntity.badRequest().body("파일 등록 후 다시 시도해주세요");
//        }
//
//        String fileName = file.getOriginalFilename();
//        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//
//        if (service.isExtensionBlocked(fileExtension)) {
//            log.warn("업로드 실패. {} 확장자 검출", fileExtension);
//            return ResponseEntity.badRequest().body("해당 파일은 차단된 확장자 입니다.");
//        }
//
//        if (service.isFileAlreadyUploaded(fileName)) {
//            log.warn("업로드 실패. {} 파일명 중복", fileName);
//            return ResponseEntity.badRequest().body("이미 등록된 파일입니다.");
//        }
//
//        // 여기에 파일 저장 로직 구현 (생략)
//        service.addUploadedFile(fileName);
//        log.info("파일 업로드 성공. {}", fileName);
//        return ResponseEntity.ok().body("파일 업로드 성공: " + fileName);
//    }



//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
//        log.warn("파일 업로드 실패: 최대 크기 초과", e);
//        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("파일 크기가 너무 큽니다. 더 작은 파일을 선택해주세요.");
//    }

//    @GetMapping("/uploaded")
//    public List<String> getUploadedFiles() {
////        log.info("Fetching uploaded files list");
//        return service.getUploadedFiles();
//    }
}