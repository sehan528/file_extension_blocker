package org.example.file_extension_blocker.service;

import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.dto.FileDTO;
import org.example.file_extension_blocker.model.File;
import org.example.file_extension_blocker.repository.FileRepository;
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
class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private BlockedExtensionService blockedExtensionService;

    @BeforeEach
    void setUp() {
        fileRepository.deleteAll();
        fileRepository.saveAll(List.of(
                new File(null, "test1.txt"),
                new File(null, "test2.pdf"),
                new File(null, "test3.doc")
        ));
    }

    @Test
    @DisplayName("파일 목록 불러오기 테스트")
    void getAllFiles() {
        List<FileDTO> files = fileService.getAllFiles();
        assertEquals(3, files.size());
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("test1.txt")));
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("test2.pdf")));
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("test3.doc")));
    }

    @Test
    @DisplayName("새 파일 추가 테스트")
    void addFile() {
        FileDTO fileDTO = new FileDTO(null, "newfile.txt");
        fileService.addFile(fileDTO);

        List<FileDTO> files = fileService.getAllFiles();
        assertEquals(4, files.size());
        assertTrue(files.stream().anyMatch(file -> file.getName().equals("newfile.txt")));
    }

    @Test
    @DisplayName("차단된 확장자 파일 추가 예외 발생 테스트")
    void addFile_BlockedExtension() {
        // 먼저 pdf 확장자를 차단 목록에 추가
        blockedExtensionService.addCustomExtension(new BlockedExtensionDTO(null, "pdf", false, true));

        FileDTO fileDTO = new FileDTO(null, "blockedfile.pdf");
        assertThrows(IllegalArgumentException.class, () -> fileService.addFile(fileDTO));
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void deleteFile() {
        File fileToDelete = fileRepository.findAll().get(0);
        FileDTO fileDTO = new FileDTO(fileToDelete.getId(), fileToDelete.getName());

        fileService.deleteFile(fileDTO);

        List<FileDTO> files = fileService.getAllFiles();
        assertEquals(2, files.size());
        assertFalse(files.stream().anyMatch(file -> file.getName().equals(fileToDelete.getName())));
    }

    @Test
    @DisplayName("존재하지 않는 파일 삭제 시도 중 예외 발생 테스트")
    void deleteFile_NotFound() {
        FileDTO nonExistentFile = new FileDTO(9999L, "nonexistent.txt");
        assertThrows(IllegalArgumentException.class, () -> fileService.deleteFile(nonExistentFile));
    }

    @Test
    @DisplayName("특정 확장자가 사용 중 확인 테스트")
    void isExtensionUsed() {
        assertTrue(fileService.isExtensionUsed("txt"));
        assertTrue(fileService.isExtensionUsed("pdf"));
        assertTrue(fileService.isExtensionUsed("doc"));
        assertFalse(fileService.isExtensionUsed("jpg"));
    }
}