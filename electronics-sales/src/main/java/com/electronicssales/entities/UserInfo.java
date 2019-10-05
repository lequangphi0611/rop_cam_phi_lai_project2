package com.electronicssales.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "user_infos")
@Data
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

    @OneToOne(mappedBy = "userInfo", fetch = FetchType.LAZY)
    private User user;
    
}