package com.electronicssales.entities;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "categories"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(50)",
        unique = true,
        nullable = false
    )
    String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;

    @JsonIgnore
    @OneToMany(
        mappedBy = "category", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductCategory> productCategories;

    @JsonIgnore
    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    @JoinTable(
        name = "categories_parameter_types",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "parameter_type_id")
    )
    Collection<ParameterType> parameterTypes;

    @JsonIgnore
    @OneToMany(
        mappedBy = "parent", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<Category> childrens;

    @JsonIgnore
    @OneToMany(
        fetch = FetchType.LAZY, 
        mappedBy = "category",
        cascade = CascadeType.REMOVE
    )
    Collection<CategoryManufacturer> categoryManufacturers;
 
    public Category(long id) {
        this.id = id;
    }
    
}