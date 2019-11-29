package com.electronicssales.entities;

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
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.electronicssales.models.UserProjections;
import com.electronicssales.models.types.Role;

import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "users"
)
@Where(clause = "actived=true")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SqlResultSetMapping(
    name = "UserProjectionsMapping",
    classes = @ConstructorResult(
        targetClass = UserProjections.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "lastname", type = String.class),
            @ColumnResult(name = "firstname", type = String.class),
            @ColumnResult(name = "username", type = String.class),
            @ColumnResult(name = "gender", type = Boolean.class),
            @ColumnResult(name = "birthday", type = Date.class),
            @ColumnResult(name = "email", type = String.class),
            @ColumnResult(name = "phoneNumber", type = String.class),
            @ColumnResult(name = "address", type = String.class),
            @ColumnResult(name = "avartar", type = byte[].class),
        }
    )
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToOne (
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
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

    public User(long id) {
        this.id = id;
    }

    public static User of(long id) {
        return new User(id);
    }   

}