package org.example.file_extension_blocker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blocked_extensions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false)
    private boolean isFixedExtension;

    @Column(nullable = false)
    private boolean isChecked;

}