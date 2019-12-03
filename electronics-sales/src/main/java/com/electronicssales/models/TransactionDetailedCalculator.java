package com.electronicssales.models;

import java.util.Objects;
import java.util.Optional;

import com.electronicssales.models.types.DiscountType;

public interface TransactionDetailedCalculator {

    long MIN_TRANSACTION_AMOUNT_VALUE = 0L;

    <T> T getDiscountType();

    Long getDiscountValue();

    long getPrice();

    int getQuantity();

    default Optional<DiscountType> getDiscountTypeFromAnyDiscountType() {

        Object discountTypeObject = getDiscountType();

        if(Objects.isNull(discountTypeObject)) {
            return Optional.empty();
        }

        if(discountTypeObject instanceof String) {
            return DiscountType.of((String) discountTypeObject);
        }

        if(discountTypeObject instanceof DiscountType) {
            return Optional.of((DiscountType) discountTypeObject);
        }

        return Optional.empty();
    }
    
    default Long getDiscount() {
        Optional<DiscountType> discountTypeOptional = getDiscountTypeFromAnyDiscountType();
        if(!discountTypeOptional.isPresent()) {
            return MIN_TRANSACTION_AMOUNT_VALUE;
        }
        
        if(discountTypeOptional.get() == DiscountType.AMOUNT) {
            return this.getDiscountValue();
        }

        return this.getPrice() * this.getDiscountValue() / 100;
    }

    default Long getSumDiscount() {
        return this.getDiscount() * this.getQuantity();
    }

    default Long getSubTotal() {
        return this.getPrice() * this.getQuantity();
    }

    default Long getTotal() {
        return this.getSubTotal() - this.getSumDiscount();
    }
}