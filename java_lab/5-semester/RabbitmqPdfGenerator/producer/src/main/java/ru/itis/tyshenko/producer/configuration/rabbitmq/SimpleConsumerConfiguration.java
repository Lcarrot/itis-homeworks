package ru.itis.tyshenko.producer.configuration.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static ru.itis.tyshenko.producer.configuration.rabbitmq.RabbitConfiguration.RPC_EXCHANGE;

@Configuration
public class SimpleConsumerConfiguration {

  public static final String SIMPLE_QUEUE = "consumer.simple";
  public static final String REPLY_QUEUE = "producer.simple";
  public static final String ROUTING_KEY = "simple";

  @Bean("simple_queue")
  public Queue simpleMessageQueue() {
    return QueueBuilder.durable(SIMPLE_QUEUE).build();
  }

  @Bean("simple_binding")
  public Binding bindSimpleQueue(@Qualifier("rpc_exchange") DirectExchange exchange) {
    return BindingBuilder.bind(simpleMessageQueue()).to(exchange).with(ROUTING_KEY);
  }

  // Set the return queue
  @Bean("simple_reply_queue")
  public Queue simpleReplyQueue() {
    return QueueBuilder.durable(REPLY_QUEUE).build();
  }

  // bind reply queue and exchanger
  @Bean
  public Binding simpleReplyBinding(@Qualifier("rpc_exchange") DirectExchange exchange) {
    return BindingBuilder.bind(simpleReplyQueue()).to(exchange).withQueueName();
  }

  @Bean("simple_template")
  public AsyncRabbitTemplate simpleTemplate(ConnectionFactory factory) {
    return new AsyncRabbitTemplate(factory, RPC_EXCHANGE, ROUTING_KEY, REPLY_QUEUE);
  }
}
