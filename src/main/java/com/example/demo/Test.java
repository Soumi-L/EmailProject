package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Test {
    int nb = 0;
    int boite = 1;
    int nb2 = 0;
    String[] credentials = new String[2];

    List<BoitAddress> boitAddresses = new ArrayList<>();


    @GetMapping("/balance")
    public String balance(@RequestParam("nbBoite") int nbBoite, @RequestParam("nbEnvois") int nbEnvois) {

        System.out.printf(getForm(nbEnvois,nbBoite));
     /*   nb++;
        if (nb <= nbEnvois) {
            nb2++;
            //credentials = getCredentials(boite);
            System.out.printf("< === >" + boite);
        } else {
            nb2++;
            boite++;
            nb = 1;
            // credentials = getCredentials(boite);
            System.out.printf("< === >" + boite);
        }
        if (nb2 == nbBoite * nbEnvois) {
            nb2 = nb = 0;
            boite = 1;
        }*/

        return "sent ....";
    }

    public String[] getCredentials(int position) {
        String[] credentials = new String[2];
        credentials[0] = boitAddresses.stream().filter(boitAddress -> boitAddress.getCodeBoite() == position).map(BoitAddress::getAddresseMail).toString();
        credentials[1] = boitAddresses.stream().filter(boitAddress -> boitAddress.getCodeBoite() == position).map(BoitAddress::getPasswordMail).toString();
        return credentials;
    }
    String str="";
    public String getForm(int nbEnvois, int nbBoite) {
        nb++;
        if (nb <= nbEnvois) {
            nb2++;
            credentials = getCredentials(boite);
            //System.out.printf("< === >" + boite);
            str= "< === >" + boite;
        } else {
            nb2++;
            boite++;
            nb = 1;
            credentials = getCredentials(boite);
           // System.out.printf("< === >" + boite);
            str= "< === >" + boite;
        }
        if (nb2 == nbBoite * nbEnvois) {
            nb2 = nb = 0;
            boite = 1;
        }
        return str;
    }


}
