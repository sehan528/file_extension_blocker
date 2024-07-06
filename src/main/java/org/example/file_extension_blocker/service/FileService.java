package org.example.file_extension_blocker.service;

import lombok.RequiredArgsConstructor;
import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.dto.FileDTO;
import org.example.file_extension_blocker.model.File;
import org.example.file_extension_blocker.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final BlockedExtensionService blockedExtensionService;

    @Transactional(readOnly = true)
    public List<FileDTO> getAllFiles() {
        List<File> files = fileRepository.findAll();
        List<FileDTO> fileDTOs = new ArrayList<>();
        for (File file : files) {
            fileDTOs.add(convertToDTO(file));
        }
        return fileDTOs;
    }

    @Transactional
    public void addFile(FileDTO fileDTO) {
        if (fileRepository.existsByName(fileDTO.getName())) {
            throw new IllegalArgumentException("이미 등록된 파일입니다: " + fileDTO.getName());
        }
        String extension = getFileExtension(fileDTO.getName());
        BlockedExtensionDTO blockedExtensionDTO = new BlockedExtensionDTO();
        blockedExtensionDTO.setName(extension);
        if (blockedExtensionService.isExtensionBlocked(blockedExtensionDTO)) {
            throw new IllegalArgumentException("차단된 확장자 파일입니다: " + extension);
        }
        File file = File.builder()
                .name(fileDTO.getName())
                .build();
        fileRepository.save(file);
    }

    @Transactional
    public void deleteFile(FileDTO fileDTO) {
        File file = fileRepository.findById(fileDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 파일을 찾을 수 없습니다.: " + fileDTO.getId()));
        fileRepository.delete(file);
    }

    @Transactional(readOnly = true)
    public boolean isExtensionUsed(String extension) {
        List<File> allFiles = fileRepository.findAll();
        for (File file : allFiles) {
            String fileExtension = getFileExtension(file.getName());
            if (fileExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOf + 1).toLowerCase();
    }

    private FileDTO convertToDTO(File file) {
        return FileDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .build();
    }
}