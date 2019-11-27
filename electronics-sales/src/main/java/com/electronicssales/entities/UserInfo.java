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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.electronicssales.models.UserInfoProjections;

import lombok.Data;

@Entity
@Table(name = "user_infos")
@Data
@SqlResultSetMapping(
    name = "UserInfoProjectionsMapping",
    classes = @ConstructorResult(
        targetClass = UserInfoProjections.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "lastname"),
            @ColumnResult(name = "firstname"),
            @ColumnResult(name = "phone_number"),
            @ColumnResult(name = "email"),
            @ColumnResult(name = "address"),
            @ColumnResult(name = "gender", type = Boolean.class),
            @ColumnResult(name = "birthday", type = Date.class)
        }
    )
)
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(255)",
        nullable = false
    )    
    private String firstname;

    @Column(columnDefinition = "nvarchar(255)")
    private String lastname;

    private String phoneNumber;

    private String email;

    @Column(columnDefinition = "nvarchar(255)")
    private String address;

    private boolean gender;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @OneToOne(
        mappedBy = "userInfo", 
        fetch = FetchType.LAZY,
        optional = false
    )
    private User user;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "customerInfo",
        cascade = CascadeType.REMOVE
    )
    private Collection<Transaction> transactions;
    
}