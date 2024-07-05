package org.example.file_extension_blocker.repository;

import org.example.file_extension_blocker.model.BlockedExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlockedExtensionRepository extends JpaRepository<BlockedExtension, Long> {
    List<BlockedExtension> findByIsFixedExtension(boolean isFixedExtension);

    Optional<BlockedExtension> findByName(String name);

    long countByIsFixedExtension(boolean isFixedExtension);
}