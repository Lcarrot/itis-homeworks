package ru.itis.tyshenko.simpleconsumer.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import ru.itis.tyshenko.simpleconsumer.generator.SimplePdfUserGenerator;
import ru.itis.tyshenko.simpleconsumer.util.SimpleConverter;

@Service
public class SimpleService {

  @Autowired
  private SimplePdfUserGenerator generator;

  @Autowired
  private SimpleConverter converter;

  private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

  public byte[] writePdfUserFromMessage(Message message) throws DocumentException, IOException {
    String fileName = message.getMessageProperties().getCorrelationId();
    String userString = (String) SerializationUtils.deserialize(message.getBody());
    assert userString != null;
    var form = converter.convert(userString);
    File file = generator.generatePdf(form, fileName);
    Path path = Paths.get(file.getPath());
    logger.info(path.toString());
    byte[] body = Files.readAllBytes(path);
    Files.delete(path);
    return body;
  }
}
