package com.electronicssales.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "categories"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(50)",
        unique = true,
        nullable = false
    )
    private String categoryName;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "category"
    )
    private Collection<Product> products;

    public Category(long id) {
        this.id = id;
    }
    
}