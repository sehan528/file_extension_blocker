package org.example.file_extension_blocker.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedExtensionDTO {
    private Long id;
    private String name;
    private boolean isFixedExtension;
    private boolean isChecked;
}