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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "products"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @Column(
        columnDefinition = "nvarchar(max)",
        nullable = false,
        unique = true
    )
    String productName;

    long price;

    int quantity;

    boolean salable;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedTime;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    Collection<ProductCategory> productCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    Manufacturer manufacturer;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<ProductDescription> productDescriptions;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<ProductImage> productImages;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<Review> reviews;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<Discount> discounts;

    public Product(long id) {
        this.id = id;
    }

}