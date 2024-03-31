package com.skillstorm.mvideoinventoryplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseStockDto {
    private String series;
    private String model;
    private Integer stock;
}
