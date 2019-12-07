package com.electronicssales.entities;

import java.util.Collection;
import java.util.Date;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.electronicssales.models.RevenueByDayStatisticalProjections;
import com.electronicssales.models.RevenueByMonthStatisticalProjections;
import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.ProjectionsContants;
import com.electronicssales.models.TransactionProjections;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(name = "TransactionProjectionsMapping", classes = @ConstructorResult(targetClass = TransactionProjections.class, columns = {
                @ColumnResult(name = "id", type = Long.class), @ColumnResult(name = "createdTime", type = Date.class),
                @ColumnResult(name = "fullname", type = String.class),
                @ColumnResult(name = "email", type = String.class),
                @ColumnResult(name = "phoneNumber", type = String.class),
                @ColumnResult(name = "address", type = String.class),
                @ColumnResult(name = "subTotal", type = Long.class),
                @ColumnResult(name = "discountTotal", type = Long.class),
                @ColumnResult(name = "sumTotal", type = Long.class), })),
        @SqlResultSetMapping(name = ProjectionsContants.REVENUE_OVER_MONTH_STATISTICAL_PROJECTIONS_MAPPING, classes = {
                @ConstructorResult(targetClass = RevenueOverMonthStatisticalProjections.class, columns = {
                        @ColumnResult(name = "month", type = Integer.class),
                        @ColumnResult(name = "revenue", type = Long.class) 
                }) 
        }),
        @SqlResultSetMapping(
        		name = ProjectionsContants.REVENUE_STATISTICAL_MAPPING_NAME,
        		classes = {
        			@ConstructorResult(
        				targetClass = RevenueStatisticalProjections.class,
        				columns = {
        					@ColumnResult(name = "year", type = Integer.class),
        					@ColumnResult(name = "minRevenue", type = Long.class),
        					@ColumnResult(name = "maxRevenue", type = Long.class),
        					@ColumnResult(name = "totalRevenue", type = Long.class)
        				}
        			)
        		}
        ),
        @SqlResultSetMapping(
        		name = ProjectionsContants.REVENUE_BY_MONTH_STATISTICAL_MAPPING_NAME,
        		classes = {
        			@ConstructorResult(
        				targetClass = RevenueByMonthStatisticalProjections.class,
        				columns = {
        					@ColumnResult(name = "month", type = Integer.class),
        					@ColumnResult(name = "year", type = Integer.class),
        					@ColumnResult(name = "minRevenue", type = Long.class),
        					@ColumnResult(name = "maxRevenue", type = Long.class),
        					@ColumnResult(name = "totalRevenue", type = Long.class)
        				}
        			)
        		}
        ),
        @SqlResultSetMapping(
        		name = ProjectionsContants.REVENUE_BY_DAY_STATISTICAL_MAPPING_NAME,
        		classes = {
        			@ConstructorResult(
        				targetClass = RevenueByDayStatisticalProjections.class,
        				columns = {
        					@ColumnResult(name = "day", type = Integer.class),
        					@ColumnResult(name = "month", type = Integer.class),
        					@ColumnResult(name = "year", type = Integer.class),
        					@ColumnResult(name = "minRevenue", type = Long.class),
        					@ColumnResult(name = "maxRevenue", type = Long.class),
        					@ColumnResult(name = "totalRevenue", type = Long.class)
        				}
        			)
        		}
        )
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(updatable = false)
    private Date createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserInfo customerInfo;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "transaction")
    private Collection<TransactionDetailed> transactionDetaileds;

}