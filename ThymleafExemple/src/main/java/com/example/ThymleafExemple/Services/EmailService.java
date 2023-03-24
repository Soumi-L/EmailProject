package com.example.ThymleafExemple.Services;

import com.example.ThymleafExemple.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service
@PropertySource("classpath:mail/emailconfig.properties")
public class EmailService implements IemailService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TemplateEngine htmlTemplateEngine;
    @Autowired
    private Environment environment;
    private static final String JAVA_MAIL_FILE = "classpath:mail/javamail.properties";
    private static String HOST = "mail.server.host";
    private static String PORT = "mail.server.port";
    private static String PROTOCOL = "mail.server.protocol";
    private static String USERNAME = "mail.server.username";
    private static String PASSWORD = "mail.server.password";

    private static String PAGE = "page.name";
    private JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    final Properties javaMailProperties = new Properties();


    public void sendHtml(User user, boolean showHeader, boolean showFooter) throws MessagingException {

        // Basic mail sender configuration, based on emailconfig.properties
        basicMailConfig();

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject(user.getSubject());
        message.setFrom(this.environment.getProperty(USERNAME));


       /* InternetAddress[] toAdresses=new InternetAddress[]{
                new InternetAddress("eee@eee.com"),
                new InternetAddress("eee@eee.com"),
                new InternetAddress("eee@eee.com")
        };*/

        message.setTo(user.getTo());

        // attach the file into user body
        message.addAttachment("logo.pdf", new ClassPathResource("sample.pdf"));

        final Context ctx = new Context();

        ctx.setVariable("user", user);
        ctx.setVariable("showHeader", showHeader);
        ctx.setVariable("showFooter", showFooter);

        /* Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);*/

        final String htmlContent = this.htmlTemplateEngine.process(this.environment.getProperty(PAGE), ctx);
        message.setText(htmlContent, true);

        // Send mail
        this.mailSender.send(mimeMessage);
    }

    private void basicMailConfig() {
        try {
            mailSender.setHost(this.environment.getProperty(HOST));
            mailSender.setPort(Integer.parseInt(this.environment.getProperty(PORT)));
            mailSender.setProtocol(this.environment.getProperty(PROTOCOL));
            mailSender.setUsername(this.environment.getProperty(USERNAME));
            mailSender.setPassword(this.environment.getProperty(PASSWORD));
            javaMailProperties.load(this.applicationContext.getResource(JAVA_MAIL_FILE).getInputStream());
            mailSender.setJavaMailProperties(javaMailProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
