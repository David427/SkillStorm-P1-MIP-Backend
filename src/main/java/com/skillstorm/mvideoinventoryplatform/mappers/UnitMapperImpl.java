package com.skillstorm.mvideoinventoryplatform.mappers;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnitMapperImpl implements Mapper<Unit, UnitDto> {

    private final ModelMapper modelMapper;

    @Autowired
    public UnitMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UnitDto mapTo(Unit unit) {
        return modelMapper.map(unit, UnitDto.class);
    }

    @Override
    public Unit mapFrom(UnitDto unitDto) {
        return modelMapper.map(unitDto, Unit.class);
    }

}
