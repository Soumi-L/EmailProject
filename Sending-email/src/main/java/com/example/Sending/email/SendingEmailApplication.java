package com.example.Sending.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class SendingEmailApplication {
    public static void main(String[] args) {
        SpringApplication.run(SendingEmailApplication.class, args);

    }
}
