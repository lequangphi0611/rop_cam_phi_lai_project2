package com.electronicssales.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "category_manyfacturers")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryManufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    Manufacturer manufacturer;

    public CategoryManufacturer() {
        super();
    }

    public CategoryManufacturer(Category category, Manufacturer manufacturer) {
        this.category = category;
        this.manufacturer = manufacturer;
    }

    public static CategoryManufacturer of(Category category, Manufacturer manufacturer) {
        return new CategoryManufacturer(category, manufacturer);
    }
    
}