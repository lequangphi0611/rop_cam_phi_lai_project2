package com.electronicssales.models;

import java.util.Date;

import lombok.Value;

@Value
public class TransactionProjections {

    Long id;

    Date createdTime;

    String fullname;

    String email;

    String phoneNumber;

    String address;

    long subTotal;

    long discountTotal;

    long sumTotal;
    
}