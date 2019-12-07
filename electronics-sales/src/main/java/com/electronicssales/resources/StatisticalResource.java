package com.electronicssales.resources;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronicssales.services.StatisticalService;

@RestController
@RequestMapping("/api/statisticals")
public class StatisticalResource {
	
	@Autowired
	private StatisticalService statisticalService;

	@GetMapping("category")
	public Callable<ResponseEntity<?>> statisticalCategory() {
		return () -> ResponseEntity.ok(statisticalService.getCategoryStatistical());
	}
	
	@GetMapping("revenue-product")
	public Callable<ResponseEntity<?>> getRevenueProductstatistical(@RequestParam(value = "top", defaultValue = "5") Integer top) {
		return () -> ResponseEntity.ok(statisticalService.getRevenueProductStatistical(top));
	}
	
	@GetMapping("revenue-month")
	public Callable<ResponseEntity<?>> getRevenueOverMonthStatistical() {
		return () -> ResponseEntity.ok(statisticalService.getRevenueOverMonthStatistical());
	}
}
