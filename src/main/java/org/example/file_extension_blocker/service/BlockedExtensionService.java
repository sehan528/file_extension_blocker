package org.example.file_extension_blocker.service;

import lombok.RequiredArgsConstructor;
import org.example.file_extension_blocker.dto.BlockedExtensionDto;
import org.example.file_extension_blocker.model.BlockedExtension;
import org.example.file_extension_blocker.repository.BlockedExtensionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockedExtensionService {
    private final BlockedExtensionRepository repository;

    @Transactional(readOnly = true)
    public List<BlockedExtension> getFixedExtensions() {
        return repository.findByIsFixedExtension(true);
    }

    @Transactional(readOnly = true)
    public List<BlockedExtension> getCustomExtensions() {
        return repository.findByIsFixedExtension(false);
    }

    @Transactional
    public void toggleFixedExtension(String name, boolean checked) {
        BlockedExtension extension = repository.findByName(name.toLowerCase())
                .orElseGet(() -> new BlockedExtension(name.toLowerCase(), true, false));
        extension.setChecked(checked);
        repository.save(extension);
    }

    @Transactional
    public void addCustomExtension(String name) {
        name = name.toLowerCase();
        if (name.length() > 20) {
            throw new IllegalArgumentException("확장자 이름은 20자를 초과할 수 없습니다.");
        }
        if (repository.countByIsFixedExtension(false) >= 200) {
            throw new IllegalStateException("커스텀 확장자는 최대 200개까지만 등록 가능합니다.");
        }
        if (repository.existsByName(name)) {
            throw new IllegalArgumentException("이미 등록된 확장자입니다: " + name);
        }
        repository.save(new BlockedExtension(name, false, true));
    }

    @Transactional
    public void deleteCustomExtension(String name) {
        BlockedExtension extension = repository.findByName(name.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Extension not found: " + name));
        if (extension.isFixedExtension()) {
            throw new IllegalArgumentException("고정 확장자는 삭제할 수 없습니다: " + name);
        }
        repository.delete(extension);
    }

    @Transactional(readOnly = true)
    public boolean isExtensionBlocked(String extension) {
        Optional<BlockedExtension> blockedExtension = repository.findByName(extension.toLowerCase());
        return blockedExtension.map(BlockedExtension::isChecked).orElse(false);
    }

//    ---
//    private List<String> uploadedFiles = new ArrayList<>();
//    private boolean isValidExtension(String name) {
//        return name.matches("^[a-z0-9]+$");
//    }
//
//    private String sanitizeInput(String input) {
//        // 여기서는 간단한 이스케이프 처리만 수행합니다.
//        // 실제 환경에서는 더 강력한 라이브러리(예: OWASP Java Encoder)를 사용하는 것이 좋습니다.
//        return input.replaceAll("[<>&'\"]", "");
//    }
//
//    private boolean isFixedExtension(String name) {
//        return repository.findByName(name)
//                .map(BlockedExtension::isFixedExtension)
//                .orElse(false);
//    }
//
//    public boolean isFileAlreadyUploaded(String fileName) {
//        return uploadedFiles.contains(fileName);
//    }
//
//    public void addUploadedFile(String fileName) {
//        uploadedFiles.add(fileName);
//    }
//
//    public List<String> getUploadedFiles() {
//        return new ArrayList<>(uploadedFiles);
//    }
}