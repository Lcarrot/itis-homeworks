package ru.itis.tyshenko.producer.controller;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.tyshenko.producer.dto.FileMetadataDto;
import ru.itis.tyshenko.producer.service.FileService;

@RestController
public class PdfController {

  @Autowired
  private FileService service;

  @PostMapping(value = "/generate/{type}")
  public ResponseEntity<Resource> generate(@PathVariable String type, Authentication authentication)
      throws ExecutionException, InterruptedException {
    String id = UUID.randomUUID().toString();
    Optional<ByteArrayResource> object = service.generateFile(type, authentication, id);
    if (object.isEmpty()) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok()
        .header("Metadata-Id", id)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".pdf" + "\"")
        .contentLength(object.get().contentLength())
        .contentType(MediaType.APPLICATION_PDF)
        .body(object.get());
  }

  @GetMapping(value = "/metadata/{id}")
  public ResponseEntity<FileMetadataDto> getMetadata(@PathVariable String id, Authentication authentication) {
    Optional<FileMetadataDto> dto = service.getMetadata(id, authentication);
    return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
  }
}
