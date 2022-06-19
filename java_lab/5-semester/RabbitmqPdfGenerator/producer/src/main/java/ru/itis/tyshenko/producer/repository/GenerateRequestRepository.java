package ru.itis.tyshenko.producer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.tyshenko.producer.model.FileMetadata;

public interface GenerateRequestRepository extends JpaRepository<FileMetadata, Long> {

  FileMetadata getFileMetadataByFileName(String id);
}
