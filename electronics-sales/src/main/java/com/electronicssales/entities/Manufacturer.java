package com.electronicssales.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manufacturers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer implements Serializable {

    private static final long serialVersionUID = -6505611351764323974L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(50)",
        nullable = false,
        unique = true
    )
    private String manufacturerName;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinColumn(name = "logo_id")
    private Image manufacturerLogo;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "manufacturer",
        cascade = CascadeType.REMOVE
    )
    Collection<CategoryManufacturer> categoryManufacturers;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE,
        mappedBy = "manufacturer"
    )
    private Collection<Product> products;

    public Manufacturer(long id) {
        this.id = id;
    }

    public static Manufacturer of(long id) {
        return new Manufacturer(id);
    }
    
}