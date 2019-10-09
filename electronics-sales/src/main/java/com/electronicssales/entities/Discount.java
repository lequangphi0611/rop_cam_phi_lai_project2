package com.electronicssales.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.electronicssales.models.types.DiscountType;

import lombok.Data;

@Entity
@Table(name = "discounts")
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedTime;

    @OneToMany(
        mappedBy = "discount",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductDiscount> productDiscounts;
    
}