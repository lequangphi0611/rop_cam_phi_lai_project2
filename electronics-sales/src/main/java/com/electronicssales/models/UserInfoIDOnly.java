package com.electronicssales.models;

import org.springframework.beans.factory.annotation.Value;

public interface UserInfoIDOnly {

    @Value("#{target.userInfo.id}")
    Long getUserInfoId();
    
}