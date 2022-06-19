package ru.itis.tyshenko.harderconsumer.listener;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.harderconsumer.service.HarderService;

@Component
public class RabbitListener {

  private static final Logger logger = LoggerFactory.getLogger(RabbitListener.class);

  @Autowired
  private HarderService harderService;

  @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "consumer.harder")
  public Message onMessage(Message message) throws DocumentException, IOException {
    logger.info(message.getMessageProperties().toString());
    byte[] body = harderService.writePdfUserFromMessage(message);
    return new Message(body);
  }
}
