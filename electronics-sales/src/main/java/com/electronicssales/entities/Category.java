package com.electronicssales.entities;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
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
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import com.electronicssales.models.CategoryStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;

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
@SqlResultSetMappings(
	value = {
		@SqlResultSetMapping(
				name = ProjectionsContants.CATEGORY_STATISTICAL_MAPPING_NAME,
				classes = {
						@ConstructorResult(
								targetClass = CategoryStatisticalProjections.class,
								columns = {
										@ColumnResult(name = "categoryName", type = String.class),
										@ColumnResult(name = "productCount", type = Integer.class),
										@ColumnResult(name = "totalProductSold", type = Integer.class)
								}
						)
				}
		)
	}
)
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

    @OneToMany(
        mappedBy = "category", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<ProductCategory> productCategories;

    @ManyToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinTable(
        name = "categories_parameter_types",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "parameter_type_id")
    )
    Collection<ParameterType> parameterTypes;

    @OneToMany(
        mappedBy = "parent", 
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE
    )
    Collection<Category> childrens;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "category"
    )
    Collection<CategoryManufacturer> categoryManufacturers;
 
    public Category(long id) {
        this.id = id;
    }

    public static Category of(long id) {
        return new Category(id);
    }
    
}