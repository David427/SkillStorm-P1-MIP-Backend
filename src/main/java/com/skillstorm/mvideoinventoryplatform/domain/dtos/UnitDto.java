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
    private String vram;
    private String factoryClock;
    private String videoCores;
    private String powerDraw;
    private WarehouseDto warehouse;

}
