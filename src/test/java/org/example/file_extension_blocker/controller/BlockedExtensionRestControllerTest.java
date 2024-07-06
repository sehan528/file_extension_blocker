package org.example.file_extension_blocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.file_extension_blocker.dto.BlockedExtensionDTO;
import org.example.file_extension_blocker.model.BlockedExtension;
import org.example.file_extension_blocker.repository.BlockedExtensionRepository;
import org.example.file_extension_blocker.service.BlockedExtensionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BlockedExtensionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlockedExtensionService service;

    @Autowired
    private BlockedExtensionRepository repository;

    @BeforeEach
    void setUp() {
        // 테스트 전에 데이터베이스를 초기화하고 테스트 데이터를 삽입
        repository.deleteAll();
        BlockedExtension extension = new BlockedExtension(1L, "exe", true, true);
        repository.save(extension);
    }


    @Test
    @DisplayName("고정 확장자 토글 테스트")
    void toggleFixedExtension() throws Exception {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(1L, "exe", true, false);

        mockMvc.perform(post("/api/extensions/fixed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());


        BlockedExtension updatedExtension = repository.findByName("exe").orElseThrow();
        assertFalse(updatedExtension.isChecked());
    }

    @Test
    @DisplayName("커스텀 확장자 추가 테스트")
    void addCustomExtension() throws Exception {
        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "png", false, true);

        mockMvc.perform(post("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("커스텀 확장자 삭제 테스트")
    void deleteCustomExtension() throws Exception {
        // 먼저 커스텀 확장자 추가
        service.addCustomExtension(new BlockedExtensionDTO(null, "png", false, true));

        BlockedExtensionDTO dto = new BlockedExtensionDTO(null, "png", false, true);

        mockMvc.perform(delete("/api/extensions/custom")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}