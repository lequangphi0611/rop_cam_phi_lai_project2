package com.electronicssales.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    Long id;

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
    @Column(updatable = false)
    Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    Date updatedTime;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductCategory> productCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    Manufacturer manufacturer;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductImage> productImages;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<Review> reviews;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductParameter> productParameters;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
        name = "product_descriptions",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "paragraph_id")
    )
    Collection<Paragraph> descriptions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    Discount discount;

    public Product(long id) {
        this.id = id;
    }

}