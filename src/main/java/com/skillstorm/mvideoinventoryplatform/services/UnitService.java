package com.skillstorm.mvideoinventoryplatform.services;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import com.skillstorm.mvideoinventoryplatform.exceptions.*;

import java.util.List;

public interface UnitService {


    UnitDto create(UnitDto unitDto) throws Exception;

    List<UnitDto> findAll();

    List<UnitDto> findAllByWarehouse(String idCode);

    UnitDto findById(Long id) throws UnitNotFoundException;

    UnitDto fullUpdate(UnitDto unitDto) throws UnitNotFoundException;

    UnitDto partialUpdate(UnitDto unitDto) throws UnitNotFoundException;

    UnitDto transferUnit(Long id, String idCode) throws Exception;

    void delete(Long id) throws UnitNotFoundException;

    boolean isExisting(Long id);

    void determineSpecs(Unit unit);

}
