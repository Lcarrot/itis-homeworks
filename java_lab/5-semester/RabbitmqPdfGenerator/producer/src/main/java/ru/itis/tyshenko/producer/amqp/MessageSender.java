package ru.itis.tyshenko.producer.amqp;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AsyncAmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

@Component
public class MessageSender {

  private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);

  private final Map<String, AsyncAmqpTemplate> templateMap;

  private final String REPLACE_BEAN_TARGET = "_template";

  public MessageSender(List<AsyncRabbitTemplate> templates) {
    this.templateMap = templates.stream()
        .peek(asyncRabbitTemplate -> asyncRabbitTemplate.setBeanName(
            asyncRabbitTemplate.getBeanName().replace(REPLACE_BEAN_TARGET, "")))
        .collect(Collectors.toMap(AsyncRabbitTemplate::getBeanName, Function
            .identity()));
  }

  public Optional<ByteArrayResource> sendAndReceive(String routingKey, Object pdfUser, String id)
      throws ExecutionException, InterruptedException {
    if (templateMap.containsKey(routingKey)) {
      var consumer = templateMap.get(routingKey);
      var responseMessage = sendRpcMessage(pdfUser, consumer, id);
      logger.info(responseMessage.getMessageProperties().toString());
      return Optional.of(new ByteArrayResource(responseMessage.getBody()));
    }
    return Optional.empty();
  }

  private Message sendRpcMessage(Object object, AsyncAmqpTemplate template, String id)
      throws ExecutionException, InterruptedException {
    var requestMessage = convertObjectToMessage(object, id);
    logger.info(requestMessage.getMessageProperties().toString());
    return template.sendAndReceive(requestMessage).get();
  }

  private Message convertObjectToMessage(Object object, String id) {
    Message message = new Message(Objects.requireNonNull(SerializationUtils.serialize(object)));
    MessageProperties properties = message.getMessageProperties();
    properties.setCorrelationId(id);
    return message;
  }
}
