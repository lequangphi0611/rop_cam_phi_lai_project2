package com.electronicssales.repositories;

import org.springframework.stereotype.Repository;

import com.electronicssales.entities.Transaction;

@Repository
public interface StatisticalRepository extends 
	org.springframework.data.repository.Repository<Transaction, Long> ,
		BaseStatisticalRepository ,
		RevenueStatisticalRepository,
		ProductStatisticalRepository{

}
