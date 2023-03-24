package com.example.ThymleafExemple.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    private String to;
    private String cc;
    private List<String> toList;
    private List<String> ccList;
    private String name;
    private String subject;
    private String body;
    private String gender;
}
