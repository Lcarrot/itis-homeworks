package ru.itis.tyshenko.producer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.tyshenko.producer.model.FileMetadata;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadataDto {

  private String type;

  private String fileName;

  private UserDto user;

  public static FileMetadataDto from(FileMetadata metadata) {
    return FileMetadataDto.builder()
        .fileName(metadata.getFileName())
        .type(metadata.getType())
        .user(UserDto.from(metadata.getUser()))
        .build();
  }
}
