package com.electronicssales.entities;

import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.electronicssales.models.ImportInvoiceProjections;
import com.electronicssales.models.ProjectionsContants;

import lombok.Data;

@Entity
@Table(name = "import_invoices")
@Data
@SqlResultSetMapping(
	name = ProjectionsContants.IMPORT_INVOICE_MAPPING_NAME,
	classes = {
		@ConstructorResult(
			targetClass = ImportInvoiceProjections.class,
			columns = {
				@ColumnResult(name = "id", type = Long.class),
				@ColumnResult(name = "importTime", type = Date.class),
				@ColumnResult(name = "quantity", type = Integer.class),
				@ColumnResult(name = "creatorUsername", type = String.class),
				@ColumnResult(name = "productName", type = String.class),
				@ColumnResult(name = "productImage", type = byte[].class),
				@ColumnResult(name = "creatorAvartar", type = byte[].class),
			}
		)
	}
)
public class ImportInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp   
    private Date importTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

}