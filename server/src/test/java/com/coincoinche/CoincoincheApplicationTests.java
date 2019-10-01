package com.coincoinche;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoincoincheApplicationTests {

  @Autowired private HelloController controller;

  @Test
  public void contextLoads() {
    assertThat(controller).isNotNull();
  }

  @Test
  public void greetsUser() {
    String result = controller.greet();
    assertThat(result).isEqualTo("Greetings from Spring Boot!");
  }
}