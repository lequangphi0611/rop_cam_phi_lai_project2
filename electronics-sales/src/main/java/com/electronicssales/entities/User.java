package com.electronicssales.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.electronicssales.models.types.Role;

import lombok.Data;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "username",
            "phoneNumber",
            "email"
        })
    }
)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(255)",
        nullable = false
    )
    private String firstname;

    @Column(
        columnDefinition = "nvarchar(255)"
    )
    private String lastname;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avartar_id")
    private Image avartar;

    @Column(nullable = false)
    private String phoneNumber;

    private String email;

    @Column(
        columnDefinition = "nvarchar(max)",
        nullable = false
    )
    private String address;

    private boolean gender;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private String password;

    private boolean actived;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
}