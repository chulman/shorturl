package com.chulm.shorturl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class RunApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }
}
