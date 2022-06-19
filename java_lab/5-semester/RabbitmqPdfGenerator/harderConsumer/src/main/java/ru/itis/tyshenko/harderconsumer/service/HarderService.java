package ru.itis.tyshenko.harderconsumer.service;

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
import ru.itis.tyshenko.harderconsumer.generator.HarderPdfUserGenerator;
import ru.itis.tyshenko.harderconsumer.util.SimpleConverter;

@Service
public class HarderService {

  private static final Logger logger = LoggerFactory.getLogger(HarderService.class);

  @Autowired
  private HarderPdfUserGenerator generator;

  @Autowired
  private SimpleConverter converter;

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
