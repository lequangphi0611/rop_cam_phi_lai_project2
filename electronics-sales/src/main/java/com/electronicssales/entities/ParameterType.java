package com.electronicssales.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public ParameterType(long id) {
        this.id = id;
    }

}