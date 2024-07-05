package org.example.file_extension_blocker.repository;

import org.example.file_extension_blocker.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByName(String name);
    boolean existsByName(String name);
}