package com.electronicssales.resources;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountFullResponse;
import com.electronicssales.models.responses.DiscountResponse;
import com.electronicssales.services.DiscountService;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discounts")
public class DiscountResource {

    @Lazy
    @Autowired
    private DiscountService discountService;

    @Lazy
    @Autowired
    private Mapper<DiscountResponse, Discount> discountResponseMapper;

    @Lazy
    @Autowired
    private Mapper<DiscountFullResponse, Discount> discountFullResponseMapper;

    @PostMapping
    public ResponseEntity<?> createDiscount(@RequestBody DiscountDto discountDto) {
        return ResponseEntity
            .created(null)
            .body(discountResponseMapper.mapping(discountService.saveDiscount(discountDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiscount(
        @RequestBody DiscountDto discountDto,
        @PathVariable("id") long id
    ) {
        discountDto.setId(id);
        return ResponseEntity
            .ok(discountResponseMapper.mapping(discountService.updateDiscount(discountDto)));
    }

    @GetMapping
    public ResponseEntity<?> fetchDiscounts() {
        return ResponseEntity
            .ok(discountService.fetchDiscounts());
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<?> fetchProducts(@PathVariable("id") long discountId) {
        return ResponseEntity
            .ok(discountService.getProducts(discountId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOneDiscount(@PathVariable("id") long discountId) {

        return ResponseEntity
            .of(discountService
                .findById(discountId)
                .map(discountFullResponseMapper::mapping)
            );
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable("id") long discountId) {
        discountService.deleteById(discountId);
        return ResponseEntity.ok().build();
    }
}