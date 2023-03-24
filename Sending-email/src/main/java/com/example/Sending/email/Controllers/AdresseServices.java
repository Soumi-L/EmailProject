package com.example.Sending.email.Controllers;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class AdresseServices {
    private void showFailedException(SendFailedException sfe) {

        String error = sfe.getMessage() + "\n";

        Address[] ia = sfe.getInvalidAddresses();

        if (ia != null) {
            for (int x = 0; x < ia.length; x++) {
                error += "Invalid Address: " + ia[x].toString() + "\n";
            }
        }
    }
}
