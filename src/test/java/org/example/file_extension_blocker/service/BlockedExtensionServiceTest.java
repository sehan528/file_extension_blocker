package org.example.file_extension_blocker.service;

import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.model.BlockedExtension;
import org.example.file_extension_blocker.repository.BlockedExtensionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BlockedExtensionServiceTest {

    @Autowired
    private BlockedExtensionService service;

    @Autowired
    private BlockedExtensionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.saveAll(List.of(
                new BlockedExtension(null, "exe", true, true),
                new BlockedExtension(null, "bat", true, false),
                new BlockedExtension(null, "pdf", false, true),
                new BlockedExtension(null, "doc", false, true)
        ));
    }

    @Test
    @DisplayName("고정 확장자 목록 불러오기 테스트")
    void getFixedExtensions() {
        List<BlockedExtension> fixedExtensions = service.getFixedExtensions();
        assertEquals(2, fixedExtensions.size());
        assertTrue(fixedExtensions.stream().allMatch(BlockedExtension::isFixedExtension));
    }

    @Test
    @DisplayName("커스텀 확장자 목록 불러오기 테스트")
    void getCustomExtensions() {
        List<BlockedExtension> customExtensions = service.getCustomExtensions();
        assertEquals(2, customExtensions.size());
        assertTrue(customExtensions.stream().noneMatch(BlockedExtension::isFixedExtension));
    }

    @Test
    @DisplayName("고정 확장자 토클 액션 테스트")
    void toggleFixedExtension() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "exe", true, false);
        service.toggleFixedExtension(dto);

        BlockedExtension updatedExt = repository.findByName("exe").orElseThrow();
        assertFalse(updatedExt.isChecked());
    }

    @Test
    @DisplayName("커스텀 확장자 추가 테스트")
    void addCustomExtension() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "txt", false, true);
        service.addCustomExtension(dto);

        BlockedExtension addedExt = repository.findByName("txt").orElseThrow();
        assertFalse(addedExt.isFixedExtension());
        assertTrue(addedExt.isChecked());
    }

    @Test
    @DisplayName("커스텀 확장자 중첩 예외 확인 테스트")
    void addCustomExtension_AlreadyExists() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "pdf", false, true);
        assertThrows(IllegalArgumentException.class, () -> service.addCustomExtension(dto));
    }

    @Test
    @DisplayName("커스텀 확장자 삭제 테스트")
    void deleteCustomExtension() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "pdf", false, true);
        service.deleteCustomExtension(dto);

        assertTrue(repository.findByName("pdf").isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 커스텀 확장자 삭제 예외처리 테스트")
    void deleteCustomExtension_NotFound() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "nonexistent", false, true);
        assertThrows(IllegalArgumentException.class, () -> service.deleteCustomExtension(dto));
    }

    @Test
    @DisplayName("확장자 차단 검증 테스트")
    void isExtensionBlocked() {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "exe", true, true);
        assertTrue(service.isExtensionBlocked(dto));

        dto = new BlockedExtensionDTO(null, "bat", true, false);
        assertFalse(service.isExtensionBlocked(dto));
    }
}