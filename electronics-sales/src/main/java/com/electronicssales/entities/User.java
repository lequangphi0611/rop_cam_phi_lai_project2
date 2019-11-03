package com.electronicssales.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.electronicssales.models.types.Role;

import org.hibernate.annotations.Where;

import lombok.Data;

@Entity
@Table(
    name = "users"
)
@Where(clause = "actived=true")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JoinColumn(name = "avartar_id")
    private Image avartar;

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    private String password;

    private boolean actived;
    
    @Enumerated(EnumType.STRING)
    private Role role;
    
}