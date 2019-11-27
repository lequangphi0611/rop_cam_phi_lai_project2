package com.electronicssales.models;

import java.util.Date;

import lombok.Value;

@Value
public class UserInfoProjections {

    Long id;

    String lastname;

    String firstname;

    String phoneNumber;

    String email;

    String address;

    boolean gender;

    Date birthday;
    
}