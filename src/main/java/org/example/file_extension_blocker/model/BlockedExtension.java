package org.example.file_extension_blocker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "blocked_extensions")
@Getter
@Setter
@NoArgsConstructor
public class BlockedExtension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Column(nullable = false, name = "is_fixed_extension")
    private boolean isFixedExtension;


}