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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.electronicssales.models.RevenueProductStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.models.types.ProductStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.ColumnDefault;
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
@SqlResultSetMappings(
	value = {
		@SqlResultSetMapping(
			name = ProjectionsContants.REVENUE_PRODUCT_STATISTICAL_MAPPING_NAME,
			classes = {
				@ConstructorResult(
					targetClass = RevenueProductStatisticalProjections.class,
					columns = {
						@ColumnResult(name = "productName", type = String.class),
						@ColumnResult(name = "image", type = byte[].class),
						@ColumnResult(name = "numberOfSales", type = Integer.class),
						@ColumnResult(name = "quantityProductSold", type = Integer.class),
						@ColumnResult(name = "revenue", type = Long.class)
					}
				)
			}
		)
	}
)
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

    @Enumerated(EnumType.ORDINAL)
    ProductStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable = false)
    Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)   
    @UpdateTimestamp
    Date updatedTime;

    @ColumnDefault("1")
    boolean active;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    Collection<ProductCategory> productCategories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    Manufacturer manufacturer;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinTable(
        name = "product_images",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    Collection<Image> images;

    @OneToMany(
        mappedBy = "product",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    Collection<ProductParameter> productParameters;

    @OneToMany(
        fetch = FetchType.LAZY, 
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JoinTable(
        name = "product_descriptions",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "paragraph_id")
    )
    Collection<Paragraph> descriptions;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "discount_id")
    Discount discount;

    @OneToMany(
        cascade = CascadeType.REMOVE,
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<ImportInvoice> importInvoices;

    @OneToMany(
        cascade = CascadeType.REMOVE,
        mappedBy = "product",
        fetch = FetchType.LAZY
    )
    Collection<TransactionDetailed> transactionDetaileds;

    public Product(long id) {
        this.id = id;
    }

    public static Product of(long id) {
        return new Product(id);
    }

}