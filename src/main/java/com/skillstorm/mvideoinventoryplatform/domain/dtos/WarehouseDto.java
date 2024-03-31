package com.skillstorm.mvideoinventoryplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseDto {

    private String idCode;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private Integer squareFt;
    private Integer stock;
    private Integer capacity;

}
