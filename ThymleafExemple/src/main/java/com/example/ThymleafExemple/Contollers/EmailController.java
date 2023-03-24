package com.example.ThymleafExemple.Contollers;

import com.example.ThymleafExemple.Entities.User;
import com.example.ThymleafExemple.Services.IemailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
public class EmailController {
    @Autowired
    IemailService emailService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendMail(
            @RequestBody User user,
            @RequestParam("showHeader") boolean showHeader,
            @RequestParam("showFooter") boolean showFooter
    )
            throws MessagingException {

        emailService.sendHtml(user, showHeader, showFooter);

        return "sent....-_-...-_-..!";
    }

}
