package com.electronicssales.models;

import java.util.Date;

import lombok.Value;

@Value
public class UserProjections {

    Long id;

    String lastname;

    String firstname;

    String username;

    boolean gender;

    Date birthday;

    String email;

    String phoneNumber;

    String address;

    byte[] avartar;

    public String getFullname() {
        return String.join(" ", lastname.trim(), firstname.trim());
    }
    
}