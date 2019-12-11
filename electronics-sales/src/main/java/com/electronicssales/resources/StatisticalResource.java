package com.electronicssales.resources;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.electronicssales.models.ImportInvoiceFetchOption;
import com.electronicssales.models.ImportInvoiceFetchOption.ImportInvoiceFetchOptionBuilder;
import com.electronicssales.models.StatisticalType;
import com.electronicssales.services.StatisticalService;

@RestController
@RequestMapping("/api/statisticals")
public class StatisticalResource {
	
	private static final String DATE_PATTERN = "yyyy-MM-dd";
	
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
	
	@GetMapping("revenue")
	public Callable<ResponseEntity<?>> getRevenueStatistical(
		@RequestParam(value = "statisticalType", defaultValue = "DAY") String statisticalTypeRequest,
		Pageable pageable
	) {
		StatisticalType statisticalType = StatisticalType.of(statisticalTypeRequest).orElse(StatisticalType.DAY);
		return () -> ResponseEntity.ok(statisticalService.getRevenueStatistical(statisticalType, pageable));
	}
	
	@GetMapping("import-invoice-reports")
	public Callable<ResponseEntity<?>> getImportInvoiceReports(
			@RequestParam("fromDate")
			@DateTimeFormat(pattern = DATE_PATTERN)
			Optional<Date> fromDateOptional,
			@RequestParam("toDate")
			@DateTimeFormat(pattern = DATE_PATTERN)
			Optional<Date> toDateOptional,
			Pageable pageable
			
	) {
		ImportInvoiceFetchOptionBuilder builder =  ImportInvoiceFetchOption.builder();
		fromDateOptional.ifPresent(builder::fromDate);
		toDateOptional.ifPresent(builder::toDate);
		return () -> ResponseEntity.ok(statisticalService.getImportInvoiceReport(builder.build(), pageable));
	}
	
	@GetMapping("product")
	public Callable<ResponseEntity<?>> getProductStatistical(
			Pageable pageable
	) {
		return () -> ResponseEntity.ok(statisticalService.getProductStatistical(pageable));
	}
}
