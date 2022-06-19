package ru.itis.tyshenko.simpleconsumer.listener;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itis.tyshenko.simpleconsumer.service.SimpleService;

@Component
public class SimpleListener {

  private static final Logger logger = LoggerFactory.getLogger(SimpleListener.class);

  @Autowired
  private SimpleService simpleService;

  @RabbitListener(queues = "consumer.simple")
  public Message onMessage(Message message) throws IOException, DocumentException {
    logger.info(message.getMessageProperties().toString());
    byte[] body = simpleService.writePdfUserFromMessage(message);
    return new Message(body, message.getMessageProperties());
  }
}
