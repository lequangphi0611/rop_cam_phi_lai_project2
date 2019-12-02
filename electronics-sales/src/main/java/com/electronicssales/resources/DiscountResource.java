package com.electronicssales.resources;

import java.util.Date;
import java.util.concurrent.Callable;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountFullResponse;
import com.electronicssales.models.responses.DiscountResponse;
import com.electronicssales.services.DiscountService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discounts")
public class DiscountResource {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    @Lazy
    @Autowired
    private DiscountService discountService;

    @Lazy
    @Autowired
    private Mapper<DiscountResponse, Discount> discountResponseMapper;

    @Lazy
    @Autowired
    private Mapper<DiscountFullResponse, Discount> discountFullResponseMapper;

    @GetMapping
    public Callable<ResponseEntity<?>> fetchAll(Pageable pageable,
        @RequestParam(value = "fromDate", required = false)
        @DateTimeFormat(pattern = DATE_FORMAT_PATTERN)
        Date fromDate,
        @RequestParam(value = "toDate", required = false)
        @DateTimeFormat(pattern = DATE_FORMAT_PATTERN)
        Date toDate
    ) {
        DiscountFetchOption option = new DiscountFetchOption();
        option.setFromDate(fromDate);
        option.setToDate(toDate);
        return () -> ResponseEntity.ok(discountService.fetchAll(option, pageable));
    }

    @PostMapping
    public Callable<ResponseEntity<?>> createDiscount(@RequestBody DiscountDto discountDto) {
        return () -> ResponseEntity
            .created(null)
            .body(discountResponseMapper.mapping(discountService.saveDiscount(discountDto)));
    }

    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> updateDiscount(
        @RequestBody DiscountDto discountDto,
        @PathVariable("id") long id
    ) {
        discountDto.setId(id);
        return () -> ResponseEntity
            .ok(discountResponseMapper.mapping(discountService.updateDiscount(discountDto)));
    }

    @GetMapping("/{id}/products")
    public Callable<ResponseEntity<?>> fetchProducts(@PathVariable("id") long discountId) {
        return () -> ResponseEntity
            .ok(discountService.getProducts(discountId));
    }

    @GetMapping("/{id}")
    public Callable<ResponseEntity<?>> getOneDiscount(@PathVariable("id") long discountId) {

        return () -> ResponseEntity
            .of(discountService
                .findById(discountId)
                .map(discountFullResponseMapper::mapping)
            );
    }
    
    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<?>> deleteDiscount(@PathVariable("id") long discountId) {
        return () -> {
            discountService.deleteById(discountId);
            return ResponseEntity.ok().build();
        };
    }
}