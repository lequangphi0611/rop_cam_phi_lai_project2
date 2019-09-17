package com.electronicssales.models.types;

public enum Role {

    ADMIN,

    CUSTOMER;

    public static Role of(String roleString) {
        for(Role role : Role.values()) {
            if(role.toString().equals(roleString.toUpperCase()))
                return role;
        }

        throw new RuntimeException(roleString + " not role type !");
    } 

}