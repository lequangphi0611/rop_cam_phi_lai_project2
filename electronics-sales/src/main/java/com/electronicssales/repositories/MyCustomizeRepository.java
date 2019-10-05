package com.electronicssales.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MyCustomizeRepository<T, ID extends Serializable> 
    extends JpaRepository<T, ID> {

    <S extends T> S persist(S entity);

    <S extends T> S merge(S entity);
    
}