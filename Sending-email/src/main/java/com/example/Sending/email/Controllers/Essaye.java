package com.example.Sending.email.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
public class Essaye {

    @Autowired
    TriggerController service;

    @GetMapping("/")
    public String notif(@RequestParam("to") String to,@RequestParam("subject") String subject,@RequestParam("body") String body,@RequestParam("gender")boolean gender) throws MessagingException, IOException {
        service.mailSender(to, subject, body,gender);
        return "Sent .................";
    }


   /* @Bean
    public void msg(){
         System.out.println( "=============================HELLO WORLD==========================================");
    }*/
}
