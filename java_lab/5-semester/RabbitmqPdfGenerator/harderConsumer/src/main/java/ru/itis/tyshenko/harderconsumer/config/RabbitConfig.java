package ru.itis.tyshenko.harderconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class RabbitConfig {

  public static final String RPC_EXCHANGE = "rpc_exchange";
  public static final String ROUTING_KEY = "harder";

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(RPC_EXCHANGE);
  }

  /**
   * Queue and binding for dead letters
   */
  @Bean("dead_queue")
  public Queue queue() {
    return QueueBuilder.nonDurable().withArgument("x-dead-letter-exchange", "deadLetterExchange")
        .withArgument("x-dead-letter-routing-key", "deadLetter").build();
  }

  @Bean
  public Binding binding(@Qualifier("dead_queue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
  }

  @Bean
  public RabbitListenerContainerFactory<SimpleMessageListenerContainer> containerFactory(
      ConnectionFactory rabbitConnectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(rabbitConnectionFactory);
    factory.setPrefetchCount(10);
    factory.setConcurrentConsumers(5);
    return factory;
  }
}
