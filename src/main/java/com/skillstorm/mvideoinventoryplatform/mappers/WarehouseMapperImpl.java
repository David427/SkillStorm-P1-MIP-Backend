package com.skillstorm.mvideoinventoryplatform.mappers;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapperImpl implements Mapper<Warehouse, WarehouseDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public WarehouseMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public WarehouseDto mapTo(Warehouse warehouse) {
        return modelMapper.map(warehouse, WarehouseDto.class);
    }

    @Override
    public Warehouse mapFrom(WarehouseDto warehouseDto) {
        return modelMapper.map(warehouseDto, Warehouse.class);
    }

}
