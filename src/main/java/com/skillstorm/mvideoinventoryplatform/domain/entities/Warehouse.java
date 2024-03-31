package com.skillstorm.mvideoinventoryplatform.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @Column(length = 30)
    private String idCode;

    @Column(length = 100)
    private String streetAddress;

    @Column(length = 40)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(length = 5)
    private String zipCode;

    @Column
    private Integer squareFt;

    @Column
    private Integer stock;

    @Column
    private Integer capacity;

}
