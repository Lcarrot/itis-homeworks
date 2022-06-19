package ru.itis.tyshenko.producer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

class ProducerApplicationTests {

  @Test
  void contextLoads() {
  }

  @Test
  void getResources() throws FileNotFoundException {
    System.out.println(Paths.get("1").toAbsolutePath());
  }

}
