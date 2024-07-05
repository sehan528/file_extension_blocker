package org.example.file_extension_blocker.service;

import lombok.RequiredArgsConstructor;
import org.example.file_extension_blocker.model.File;
import org.example.file_extension_blocker.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final BlockedExtensionService blockedExtensionService;

    @Transactional(readOnly = true)
    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    @Transactional
    public void addFile(String fileName) {
        if (fileRepository.existsByName(fileName)) {
            throw new IllegalArgumentException("이미 등록된 파일입니다: " + fileName);
        }
        String extension = getFileExtension(fileName);
        if (blockedExtensionService.isExtensionBlocked(extension)) {
            throw new IllegalArgumentException("차단된 확장자 파일입니다: " + extension);
        }
        fileRepository.save(new File(fileName));
    }

    @Transactional
    public void deleteFile(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("File not found with id: " + id));
        fileRepository.delete(file);
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }
}