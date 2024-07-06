package org.example.file_extension_blocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.file_extension_blocker.dto.FileDTO;
import org.example.file_extension_blocker.service.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FileRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileService fileService;

    @Test
    @DisplayName("모든 파일 목록 조회 테스트")
    void getFiles() throws Exception {
        mockMvc.perform(get("/api/files"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("파일 업로드 테스트")
    void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(multipart("/api/files/upload").file(file))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("파일 삭제 테스트")
    void deleteFile() throws Exception {
        // 먼저 파일 추가
        FileDTO fileDTO = new FileDTO(null, "testfile.txt");
        fileService.addFile(fileDTO);

        // 추가된 파일의 ID 가져오기
        Long fileId = fileService.getAllFiles().stream()
                .filter(f -> f.getName().equals("testfile.txt"))
                .findFirst()
                .map(FileDTO::getId)
                .orElseThrow();

        mockMvc.perform(delete("/api/files/" + fileId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("확장자 사용 여부 확인 테스트")
    void checkExtensionUsage() throws Exception {
        mockMvc.perform(get("/api/files/usage")
                        .param("extension", "txt"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}