package ru.itis.tyshenko.producer.service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.itis.tyshenko.producer.amqp.MessageSender;
import ru.itis.tyshenko.producer.converter.UserGenerateConverter;
import ru.itis.tyshenko.producer.dto.FileMetadataDto;
import ru.itis.tyshenko.producer.form.UserGenerateForm;
import ru.itis.tyshenko.producer.model.FileMetadata;
import ru.itis.tyshenko.producer.model.User;
import ru.itis.tyshenko.producer.repository.GenerateRequestRepository;
import ru.itis.tyshenko.producer.security.details.UserDetailsImpl;
import ru.itis.tyshenko.producer.security.token.access.AccessTokenAuthentication;

@Service
public class FileService {

  @Autowired
  private MessageSender sender;

  @Autowired
  private GenerateRequestRepository requestRepository;

  @Autowired
  private UserGenerateConverter converter;

  public Optional<ByteArrayResource> generateFile(String routingKey,
      Authentication authentication, String id)
      throws ExecutionException, InterruptedException {
    var user = getAuthUser(authentication);
    var userDto = UserGenerateForm.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
    var value = converter.convert(userDto);
    Optional<ByteArrayResource> resource = sender.sendAndReceive(routingKey, value, id);
    if (resource.isPresent()) {
      FileMetadata metadata = FileMetadata.builder().fileName(id).user(user).type(routingKey).build();
      requestRepository.save(metadata);
    }
    return resource;
  }

  public Optional<FileMetadataDto> getMetadata(String id, Authentication authentication) {
    FileMetadata metadata = requestRepository.getFileMetadataByFileName(id);
    var authUser = getAuthUser(authentication);
    if (!authUser.equals(metadata.getUser())) {
      return Optional.empty();
    }
    return Optional.of(FileMetadataDto.from(metadata));
  }

  private User getAuthUser(Authentication authentication) {
    var tokenAuthentication = (AccessTokenAuthentication) authentication;
    return ((UserDetailsImpl) tokenAuthentication.getDetails()).getUser();
  }
}
