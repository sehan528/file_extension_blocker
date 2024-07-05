package org.example.file_extension_blocker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.file_extension_blocker.service.BlockedExtensionService;
import org.example.file_extension_blocker.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BlockedExtensionController {
    private final BlockedExtensionService Blockedservice;
    private final FileService fileService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("fixedExtensions", Blockedservice.getFixedExtensions());
        model.addAttribute("customExtensions", Blockedservice.getCustomExtensions());
        model.addAttribute("uploadedFiles", fileService.getAllFiles());
        return "home";
    }
}