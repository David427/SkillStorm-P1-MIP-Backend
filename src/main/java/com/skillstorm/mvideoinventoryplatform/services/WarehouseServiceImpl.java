package com.skillstorm.mvideoinventoryplatform.services;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseStockDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseAlreadyExistsException;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseNotFoundException;
import com.skillstorm.mvideoinventoryplatform.mappers.Mapper;
import com.skillstorm.mvideoinventoryplatform.repositories.UnitRepository;
import com.skillstorm.mvideoinventoryplatform.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final Mapper<Warehouse, WarehouseDto> warehouseMapper;

    private final WarehouseRepository warehouseRepository;

    private final UnitRepository unitRepository;

    @Autowired
    public WarehouseServiceImpl(Mapper<Warehouse, WarehouseDto> warehouseMapper, WarehouseRepository warehouseRepository, UnitRepository unitRepository) {
        this.warehouseMapper = warehouseMapper;
        this.warehouseRepository = warehouseRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public WarehouseDto create(WarehouseDto warehouseDto) throws WarehouseAlreadyExistsException {
        // mapFrom(): DTO -> entity
        // mapTo(): entity -> DTO

        if (isExisting(warehouseDto.getIdCode())) {
            throw new WarehouseAlreadyExistsException("This warehouse already exists.");
        }

        Warehouse warehouseEntity = warehouseMapper.mapFrom(warehouseDto);
        Warehouse savedWarehouse = warehouseRepository.save(warehouseEntity);
        return warehouseMapper.mapTo(savedWarehouse);
    }

    @Override
    public List<WarehouseDto> findAll() {
        // List will be of unknown size, most expensive operation should be insertion
        List<WarehouseDto> warehouseDtos = new LinkedList<>();
        List<Warehouse> foundWarehouses = warehouseRepository.findAll();

        for (Warehouse warehouse : foundWarehouses) {
            warehouseDtos.add(warehouseMapper.mapTo(warehouse));
        }

        return warehouseDtos;
    }

    @Override
    public WarehouseDto findById(String idCode) throws WarehouseNotFoundException {
        Optional<Warehouse> foundWarehouse = warehouseRepository.findById(idCode);

        if (foundWarehouse.isPresent()) {
            Warehouse warehouse = foundWarehouse.get();
            return warehouseMapper.mapTo(warehouse);
        } else {
            throw new WarehouseNotFoundException("Warehouse not found.");
        }
    }

    @Override
    public WarehouseDto fullUpdate(WarehouseDto warehouseDto) throws WarehouseNotFoundException {
        if (isExisting(warehouseDto.getIdCode())) {
            Warehouse warehouseEntity = warehouseMapper.mapFrom(warehouseDto);
            Warehouse updatedWarehouse = warehouseRepository.save(warehouseEntity);
            return warehouseMapper.mapTo(updatedWarehouse);
        } else {
            throw new WarehouseNotFoundException("Warehouse not found.");
        }
    }

    @Override
    public WarehouseDto partialUpdate(WarehouseDto warehouseDto) throws WarehouseNotFoundException {
        Warehouse warehouse = warehouseMapper.mapFrom(warehouseDto);

        // Only update each field if it's not null (empty Optional) in the passed-in DTO
        return warehouseRepository.findById(warehouse.getIdCode()).map(existingWarehouse -> {
            Optional.ofNullable(warehouse.getStreetAddress()).ifPresent(existingWarehouse::setStreetAddress);
            Optional.ofNullable(warehouse.getCity()).ifPresent(existingWarehouse::setCity);
            Optional.ofNullable(warehouse.getState()).ifPresent(existingWarehouse::setState);
            Optional.ofNullable(warehouse.getZipCode()).ifPresent(existingWarehouse::setZipCode);
            Optional.ofNullable(warehouse.getSquareFt()).ifPresent(existingWarehouse::setSquareFt);
            Optional.ofNullable(warehouse.getStock()).ifPresent(existingWarehouse::setStock);
            Optional.ofNullable(warehouse.getCapacity()).ifPresent(existingWarehouse::setCapacity);
            warehouseRepository.save(existingWarehouse);
            return warehouseMapper.mapTo(existingWarehouse);
        }).orElseThrow(() -> new WarehouseNotFoundException("Warehouse not found."));
    }

    @Override
    public void delete(String idCode) throws WarehouseNotFoundException {
        if (isExisting(idCode)) {
            warehouseRepository.deleteById(idCode);
        } else {
            throw new WarehouseNotFoundException("Warehouse not found.");
        }
    }

    @Override
    public boolean isExisting(String idCode) {
        return warehouseRepository.existsById(idCode);
    }

    @Override
    public List<WarehouseStockDto> getStock(String idCode) {
        List<WarehouseStockDto> stockDtos = new ArrayList<>();
        WarehouseStockDto model1Dto = WarehouseStockDto.builder()
                .series("RGX")
                .model("400")
                .stock(unitRepository.countByModelAndWarehouse("400", idCode))
                .build();
        WarehouseStockDto model2Dto = WarehouseStockDto.builder()
                .series("RGX")
                .model("600 Super")
                .stock(unitRepository.countByModelAndWarehouse("600 Super", idCode))
                .build();
        WarehouseStockDto model3Dto = WarehouseStockDto.builder()
                .series("RGX")
                .model("800 Extreme Pi")
                .stock(unitRepository.countByModelAndWarehouse("800 Extreme Pi", idCode))
                .build();

        stockDtos.add(model1Dto);
        stockDtos.add(model2Dto);
        stockDtos.add(model3Dto);

        return stockDtos;
    }

}
