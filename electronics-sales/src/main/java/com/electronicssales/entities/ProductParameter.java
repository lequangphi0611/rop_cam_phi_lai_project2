package com.electronicssales.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_type_id")
    private ParameterType parameterType;

    @Column(columnDefinition = "nvarchar(max)")
    private String parameterValue;

    public ProductParameter(Product product, ParameterType parameterType, String parameterValue) {
        this(null, product, parameterType, parameterValue);
    }

    public static ProductParameter of(Product product, ParameterType parameterType, String parameterValue) {
        return new ProductParameter(product, parameterType, parameterValue);
    }
    
}