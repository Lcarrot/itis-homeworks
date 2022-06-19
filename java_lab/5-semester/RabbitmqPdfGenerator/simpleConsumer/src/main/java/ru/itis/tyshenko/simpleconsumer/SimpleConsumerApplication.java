package ru.itis.tyshenko.simpleconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SimpleConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SimpleConsumerApplication.class, args);
  }

}
