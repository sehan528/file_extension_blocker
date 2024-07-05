package org.example.file_extension_blocker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockedExtensionDto {
    private Long id;
    private String name;
    private boolean isFixedExtension;
}