package com.electronicssales.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parameter_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(
        columnDefinition = "nvarchar(255)",
        nullable = false,
        unique = true
    )
    private String parameterTypeName;

    @JsonIgnore
    @ManyToMany(mappedBy = "parameterTypes", fetch = FetchType.LAZY)
    Collection<Category> categories;

    public ParameterType(long id) {
        this.id = id;
    }

    public static ParameterType of(long id) {
        return new ParameterType(id);
    }

}