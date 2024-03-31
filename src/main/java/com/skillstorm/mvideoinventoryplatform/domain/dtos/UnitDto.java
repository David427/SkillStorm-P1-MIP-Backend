package com.skillstorm.mvideoinventoryplatform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitDto {

    private Long id;
    private String series;
    private String model;
    private Integer vram;
    private Double factoryClock;
    private Integer videoCores;
    private Integer aiCores;
    private Integer powerDraw;
    private WarehouseDto warehouse;

}
