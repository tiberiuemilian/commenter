package com.example.commenter;

import com.example.commenter.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class CommenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommenterApplication.class, args);
    }

}
