package com.electronicssales.entities;

import javax.persistence.CascadeType;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.electronicssales.models.TransactionDetailedProjections;
import com.electronicssales.models.types.DiscountType;

import lombok.Data;

@Entity
@Table(name = "transaction_detaileds")
@Data 
@SqlResultSetMapping(
    name = "TransactionDetailedProjectionsMapping",
    classes = @ConstructorResult(
        targetClass = TransactionDetailedProjections.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "image", type = byte[].class),
            @ColumnResult(name = "productName", type = String.class),
            @ColumnResult(name = "price", type = Long.class),
            @ColumnResult(name = "quantity", type = Integer.class),
            @ColumnResult(name = "discountType", type = String.class),
            @ColumnResult(name = "discountValue", type = Long.class),
        }
    )
)
public class TransactionDetailed {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.DETACH
    )
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private long price;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private long discountValue;
    
}