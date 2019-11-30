package com.electronicssales.entities;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.electronicssales.models.DiscountProjections;
import com.electronicssales.models.types.DiscountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "discounts")
@Data
@SqlResultSetMappings(
    value = {
        @SqlResultSetMapping(
            name = "DiscountProjectionsMapping",
            classes = {
                @ConstructorResult(
                    targetClass = DiscountProjections.class,
                    columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "startedTime", type = Date.class),
                        @ColumnResult(name = "discountType", type = String.class),
                        @ColumnResult(name = "discountValue", type = Long.class),
                        @ColumnResult(name = "productCount", type = Integer.class)
                    }
                )
            }
        )
    }
)
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long discountValue;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @CreationTimestamp
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedTime;
    
    @OneToMany(
        fetch = FetchType.LAZY, 
        mappedBy = "discount",
        cascade = CascadeType.MERGE
    )
    private Collection<Product> products;
}