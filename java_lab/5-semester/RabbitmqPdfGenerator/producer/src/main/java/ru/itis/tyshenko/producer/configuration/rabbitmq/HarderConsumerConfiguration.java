package ru.itis.tyshenko.producer.configuration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.itis.tyshenko.producer.configuration.rabbitmq.RabbitConfiguration.RPC_EXCHANGE;

@Configuration
public class HarderConsumerConfiguration {

  public static final String HARDER_QUEUE = "consumer.harder";
  public static final String REPLY_QUEUE = "producer.harder";
  public static final String ROUTING_KEY = "harder";

  @Bean("harder_queue")
  public Queue harderMessageQueue() {
    return QueueBuilder.durable(HARDER_QUEUE).build();
  }

  @Bean("harder_binding")
  public Binding bindHarderQueue(@Qualifier("rpc_exchange") DirectExchange exchange) {
    return BindingBuilder.bind(harderMessageQueue()).to(exchange).with(ROUTING_KEY);
  }

  // Set the return queue
  @Bean("harder_reply_queue")
  public Queue harderReplyQueue() {
    return QueueBuilder.durable(REPLY_QUEUE).build();
  }

  // bind reply queue and exchanger
  @Bean
  public Binding harderReplyBinding(@Qualifier("rpc_exchange") DirectExchange exchange) {
    return BindingBuilder.bind(harderReplyQueue()).to(exchange).withQueueName();
  }

  @Bean("harder_template")
  public AsyncRabbitTemplate harderTemplate(ConnectionFactory factory) {
    return new AsyncRabbitTemplate(factory, RPC_EXCHANGE, ROUTING_KEY, REPLY_QUEUE);
  }
}
