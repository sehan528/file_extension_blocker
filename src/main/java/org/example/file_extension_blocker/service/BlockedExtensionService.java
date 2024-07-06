package org.example.file_extension_blocker.service;

import lombok.RequiredArgsConstructor;
import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.model.BlockedExtension;
import org.example.file_extension_blocker.repository.BlockedExtensionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockedExtensionService {
    private final BlockedExtensionRepository repository;

    @Transactional(readOnly = true)
//    public List<BlockedExtensionDTO> getFixedExtensions() {
//        List<BlockedExtension> fixedExtensions = repository.findByIsFixedExtension(true);
//        List<BlockedExtensionDTO> dtoList = new ArrayList<>();
//        for (BlockedExtension extension : fixedExtensions) {
//            dtoList.add(convertToDTO(extension));
//        }
//        return dtoList;
//    }

    public List<BlockedExtension> getFixedExtensions() {
        return repository.findByIsFixedExtension(true);
    }


//    @Transactional(readOnly = true)
//    public List<BlockedExtensionDTO> getCustomExtensions() {
//        List<BlockedExtension> customExtensions = repository.findByIsFixedExtension(false);
//        List<BlockedExtensionDTO> dtoList = new ArrayList<>();
//        for (BlockedExtension extension : customExtensions) {
//            dtoList.add(convertToDTO(extension));
//        }
//        return dtoList;
//    }

    @Transactional(readOnly = true)
    public List<BlockedExtension> getCustomExtensions() {
        return repository.findByIsFixedExtension(false);
    }

    @Transactional
    public void toggleFixedExtension(BlockedExtensionDTO dto) {
        BlockedExtension extension = repository.findByName(dto.getName())
                .orElseThrow(() -> new IllegalArgumentException("Extension not found: " + dto.getName()));

        BlockedExtension updatedExtension = BlockedExtension.builder()
                .id(extension.getId())
                .name(extension.getName())
                .isFixedExtension(extension.isFixedExtension())
                .isChecked(dto.isChecked())
                .build();

        repository.save(updatedExtension);
    }

    @Transactional
    public void addCustomExtension(BlockedExtensionDTO dto) {
        if (repository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Extension already exists: " + dto.getName());
        }

        if (dto.getName().length() > 20) {
            throw new IllegalArgumentException("확장자 이름은 20자를 초과할 수 없습니다.");
        }
        if (repository.countByIsFixedExtension(false) >= 200) {
            throw new IllegalStateException("커스텀 확장자는 최대 200개까지만 등록 가능합니다.");
        }
        if (repository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("이미 등록된 확장자입니다: " + dto.getName());
        }


        BlockedExtension extension = BlockedExtension.builder()
                .name(dto.getName())
                .isFixedExtension(false)
                .isChecked(true)
                .build();
        repository.save(extension);
    }

    @Transactional
    public void deleteCustomExtension(BlockedExtensionDTO dto) {
        BlockedExtension extension = repository.findByName(dto.getName())
                .orElseThrow(() -> new IllegalArgumentException("Extension not found: " + dto.getName()));
        if (extension.isFixedExtension()) {
            throw new IllegalArgumentException("Cannot delete fixed extension: " + dto.getName());
        }
        repository.delete(extension);
    }

    @Transactional(readOnly = true)
    public boolean isExtensionBlocked(BlockedExtensionDTO dto) {
        return repository.findByName(dto.getName())
                .map(BlockedExtension::isChecked)
                .orElse(false);
    }

//    private BlockedExtensionDTO convertToDTO(BlockedExtension extension) {
//        return BlockedExtensionDTO.builder()
//                .id(extension.getId())
//                .name(extension.getName())
//                .isFixedExtension(extension.isFixedExtension())
//                .isChecked(extension.isChecked())
//                .build();
//    }
}