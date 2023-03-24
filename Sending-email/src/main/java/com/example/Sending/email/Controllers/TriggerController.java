package com.example.Sending.email.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;


import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mail/emailconfig.properties")
public class TriggerController {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private Environment environment;

    private static final String JAVA_MAIL_FILE = "classpath:mail/javamail.properties";
    private static String HOST = "mail.server.host";
    private static String PORT = "mail.server.port";
    private static String PROTOCOL = "mail.server.protocol";
    private static String USERNAME = "mail.server.username";
    private static String PASSWORD = "mail.server.password";
    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();


    public JavaMailSender mailSender(String to, String setSubject, String setText, boolean gender) throws IOException, MessagingException {

        // Basic mail sender configuration, based on emailconfig.properties

        mailSender.setHost(this.environment.getProperty(HOST));
        mailSender.setPort(Integer.parseInt(this.environment.getProperty(PORT)));
        mailSender.setProtocol(this.environment.getProperty(PROTOCOL));
        mailSender.setUsername(this.environment.getProperty(USERNAME));
        mailSender.setPassword(this.environment.getProperty(PASSWORD));

        // JavaMail-specific mail sender configuration, based on javamail.properties
        final Properties javaMailProperties = new Properties();
        javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
        mailSender.setJavaMailProperties(javaMailProperties);

        final MimeMessage mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

        message.setFrom(USERNAME);
        message.setTo(to);
        message.setSubject(setSubject);

        int hour = LocalTime.now().getHour()+4;

        String genderValue = "Madame,";
        if (gender) genderValue = "Monsieur,";

        String greetingValue = "Bonjour ";
        if (hour>12) greetingValue = "Bonsoir ";


        message.setText(greetingValue + genderValue  +"\n"+ setText);
        mailSender.send(mimeMessage);

        System.out.println("Email sent .................");

        return mailSender;
    }

}
