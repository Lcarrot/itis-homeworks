package ru.itis.tyshenko.producer.configuration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

  public static final String RPC_EXCHANGE = "rpc_exchange";
  public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";
  public static final String DEAD_LETTER_QUEUE = "dead_letter_queue";

  @Bean
  public Queue deadLetterQueue() {
    return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange(DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding DlqBinding() {
    return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("dead_letter");
  }

  // exchanger
  @Bean("rpc_exchange")
  public DirectExchange exchange() {
    return new DirectExchange(RPC_EXCHANGE);
  }
}
