package com.electronicssales.models.responses;

import java.util.Date;

import lombok.Data;

@Data
public class UserInfo {

    private long id;

    private String firstname;

    private String lastname;

    private Date birthday;

    private long avartartId;

    private String phoneNumber;

    private String email;

    private String address;

    private boolean gender;
    
}