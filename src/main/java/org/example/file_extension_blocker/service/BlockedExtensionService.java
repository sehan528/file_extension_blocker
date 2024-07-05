package org.example.file_extension_blocker.service;

import lombok.RequiredArgsConstructor;
import org.example.file_extension_blocker.dto.BlockedExtensionDto;
import org.example.file_extension_blocker.model.BlockedExtension;
import org.example.file_extension_blocker.repository.BlockedExtensionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlockedExtensionService {
    private final BlockedExtensionRepository repository;
    private List<String> uploadedFiles = new ArrayList<>();

    @Transactional(readOnly = true)
    public List<BlockedExtension> getFixedExtensions() {
        return repository.findByIsFixedExtension(true);
    }

    @Transactional(readOnly = true)
    public List<BlockedExtension> getCustomExtensions() {
        return repository.findByIsFixedExtension(false);
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

        if (repository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 확장자입니다: " + name);
        }

        if (isFixedExtension(name)) {
            throw new IllegalArgumentException("고정 확장자는 등록할 수 없습니다: " + name);
        }

        repository.save(new BlockedExtension(name, false));
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
        return repository.findByName(extension.toLowerCase()).isPresent();
    }

    private boolean isFixedExtension(String name) {
        return repository.findByName(name)
                .map(BlockedExtension::isFixedExtension)
                .orElse(false);
    }

    public void addUploadedFile(String fileName) {
        uploadedFiles.add(fileName);
    }

    public List<String> getUploadedFiles() {
        return new ArrayList<>(uploadedFiles);
    }
}