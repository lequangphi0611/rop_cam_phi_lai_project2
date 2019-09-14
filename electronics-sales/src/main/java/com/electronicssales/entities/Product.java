package com.electronicssales.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(
    name = "products"
)
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(max)",
        nullable = false,
        unique = true
    )
    private String productName;

    private long price;

    private int quantity;

    private boolean salable;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    private Collection<ProductCategory> productCategories;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    private Collection<ProductDescription> productDescriptions;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    private Collection<ProductImage> productImages;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    private Collection<Review> reviews;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    private Collection<Discount> discounts;

}