package com.skillstorm.mvideoinventoryplatform.services;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseAlreadyExistsException;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseNotFoundException;

import java.util.List;

public interface WarehouseService {

    WarehouseDto create(WarehouseDto warehouseDto) throws WarehouseAlreadyExistsException;

    WarehouseDto findById(String idCode) throws WarehouseNotFoundException;

    List<WarehouseDto> findAll();

    WarehouseDto fullUpdate(WarehouseDto warehouseDto) throws WarehouseNotFoundException;

    WarehouseDto partialUpdate(WarehouseDto warehouseDto) throws WarehouseNotFoundException;

    void delete(String idCode) throws WarehouseNotFoundException;

    boolean isExisting(String idCode);

}
