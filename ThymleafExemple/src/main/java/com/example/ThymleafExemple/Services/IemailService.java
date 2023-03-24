package com.example.ThymleafExemple.Services;

import com.example.ThymleafExemple.Entities.User;

import javax.mail.MessagingException;

public interface IemailService {
    void sendHtml(User user, boolean showHeader, boolean showFooter) throws MessagingException;
}
