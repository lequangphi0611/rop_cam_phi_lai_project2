package com.electronicssales.repositories;

import com.electronicssales.models.UserProjections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizeUserRepository {

    Page<UserProjections> fetchAllEmployees(String seachKey, Pageable pageable);
}