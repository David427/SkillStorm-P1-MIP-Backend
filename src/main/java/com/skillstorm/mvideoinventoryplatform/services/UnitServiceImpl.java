package com.skillstorm.mvideoinventoryplatform.services;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;
import com.skillstorm.mvideoinventoryplatform.exceptions.*;
import com.skillstorm.mvideoinventoryplatform.mappers.Mapper;
import com.skillstorm.mvideoinventoryplatform.repositories.UnitRepository;
import com.skillstorm.mvideoinventoryplatform.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UnitServiceImpl implements UnitService {

    private final Mapper<Unit, UnitDto> unitMapper;

    private final UnitRepository unitRepository;

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public UnitServiceImpl(Mapper<Unit, UnitDto> unitMapper, UnitRepository unitRepository, WarehouseRepository warehouseRepository) {
        this.unitMapper = unitMapper;
        this.unitRepository = unitRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public UnitDto create(UnitDto unitDto) throws Exception {
        Unit newUnit = unitMapper.mapFrom(unitDto);
        Optional<Warehouse> warehouse = warehouseRepository.findById(unitDto.getWarehouse().getIdCode());

        // If the incoming DTO id is null, it's a new object
        if (newUnit.getId() != null) {
            if (isExisting(newUnit.getId())) {
                throw new UnitAlreadyExistsException("This unit already exists.");
            }
        }

        // Get id code from frontend, find the Warehouse in db, populate the Warehouse object in Unit, save Unit
        if (warehouse.isPresent()) {
            // Check against capacity before increasing warehouse stock
            if (Objects.equals(warehouse.get().getStock(), warehouse.get().getCapacity())) {
                throw new WarehouseAtCapacityException("Cannot add unit to warehouse. Current stock at capacity!");
            } else {
                warehouse.get().setStock(warehouse.get().getStock() + 1);
                warehouseRepository.save(warehouse.get());
            }

            newUnit.setWarehouse(warehouse.get());
        } else {
            throw new WarehouseNotFoundException("Cannot find the warehouse to add unit to.");
        }

        // Set GPU specifications based on series & model
        determineSpecs(newUnit);
        return unitMapper.mapTo(unitRepository.save(newUnit));
    }

    @Override
    public List<UnitDto> findAll() {
        List<UnitDto> unitDtos = new LinkedList<>();
        List<Unit> foundUnits = unitRepository.findAll();

        for (Unit unit : foundUnits) {
            unitDtos.add(unitMapper.mapTo(unit));
        }

        // Sort the return array (easier to do it here than in JavaScript)
        unitDtos.sort(Comparator.comparing(UnitDto::getId));
        return unitDtos;
    }

    @Override
    public List<UnitDto> findAllByWarehouse(String idCode) {
        List<UnitDto> unitDtos = new LinkedList<>();
        List<Unit> foundUnits = unitRepository.findAllByWarehouseIdCode(idCode);

        for (Unit unit : foundUnits) {
            unitDtos.add(unitMapper.mapTo(unit));
        }

        unitDtos.sort(Comparator.comparing(UnitDto::getId));
        return unitDtos;
    }

    @Override
    public UnitDto findById(Long id) throws UnitNotFoundException {
        Optional<Unit> foundUnit = unitRepository.findById(id);

        if (foundUnit.isPresent()) {
            Unit unit = foundUnit.get();
            return unitMapper.mapTo(unit);
        } else {
            throw new UnitNotFoundException("Unit not found.");
        }
    }

    @Override
    public UnitDto fullUpdate(UnitDto unitDto) throws UnitNotFoundException {
        if (isExisting(unitDto.getId())) {
            Unit unit = unitMapper.mapFrom(unitDto);
            determineSpecs(unit);
            Unit updatedUnit = unitRepository.save(unit);
            return unitMapper.mapTo(updatedUnit);
        } else {
            throw new UnitNotFoundException("Unit not found.");
        }
    }

    @Override
    public UnitDto partialUpdate(UnitDto unitDto) throws UnitNotFoundException {
        Unit unit = unitMapper.mapFrom(unitDto);

        if (unit.getSeries() != null && unit.getModel() != null) {
            determineSpecs(unit);
        }

        return unitRepository.findById(unit.getId()).map(existingUnit -> {
            Optional.ofNullable(unit.getSeries()).ifPresent(existingUnit::setSeries);
            Optional.ofNullable(unit.getModel()).ifPresent(existingUnit::setModel);
            Optional.ofNullable(unit.getVram()).ifPresent(existingUnit::setVram);
            Optional.ofNullable(unit.getFactoryClock()).ifPresent(existingUnit::setFactoryClock);
            Optional.ofNullable(unit.getVideoCores()).ifPresent(existingUnit::setVideoCores);
            Optional.ofNullable(unit.getPowerDraw()).ifPresent(existingUnit::setPowerDraw);
            unitRepository.save(existingUnit);
            return unitMapper.mapTo(existingUnit);
        }).orElseThrow(() -> new UnitNotFoundException("Unit not found."));
    }

    @Override
    public UnitDto transferUnit(Long id, String idCode) throws Exception {
        Optional<Unit> foundUnit = unitRepository.findById(id);

        if (foundUnit.isPresent()) {
            Unit unit = foundUnit.get();

            // Find the current warehouse & new warehouse
            Optional<Warehouse> currentWarehouse = warehouseRepository.findById(unit.getWarehouse().getIdCode());
            Optional<Warehouse> newWarehouse = warehouseRepository.findById(idCode);

            // Check if the Unit is already in the new warehouse
            if (currentWarehouse.isPresent() && newWarehouse.isPresent()) {
                if (unit.getWarehouse().getIdCode().equals(newWarehouse.get().getIdCode())) {
                    throw new UnitAlreadyInWarehouseException("That unit is already in the current warehouse.");
                }
            } else {
                throw new WarehouseNotFoundException("One or both warehouses could not be found.");
            }

            // Perform capacity checks & stock updates
            if (Objects.equals(newWarehouse.get().getStock(), newWarehouse.get().getCapacity())) {
                throw new WarehouseAtCapacityException("Cannot add unit to warehouse. Current stock at capacity!");
            } else {
                currentWarehouse.get().setStock(currentWarehouse.get().getStock() - 1);
                warehouseRepository.save(currentWarehouse.get());
                newWarehouse.get().setStock(newWarehouse.get().getStock() + 1);
                warehouseRepository.save(newWarehouse.get());

                unit.setWarehouse(newWarehouse.get());
                unitRepository.save(unit);
                return unitMapper.mapTo(unit);
            }
        } else {
            throw new UnitNotFoundException("Unit not found.");
        }
    }

    @Override
    public void delete(Long id) throws UnitNotFoundException {
        Optional<Unit> deletedUnit = unitRepository.findById(id);

        if (deletedUnit.isPresent()) {
            Warehouse warehouse = deletedUnit.get().getWarehouse();
            warehouse.setStock(warehouse.getStock() - 1);
            warehouseRepository.save(warehouse);
            unitRepository.deleteById(id);

        } else {
            throw new UnitNotFoundException("Unit not found.");
        }
    }

    @Override
    public boolean isExisting(Long id) {
        return unitRepository.existsById(id);
    }

    // In a real app, these values wouldn't be hardcoded
    @Override
    public void determineSpecs(Unit newUnit) {
        if (newUnit.getSeries().equals("RGX") && newUnit.getModel().equals("400")) {
            newUnit.setVram("12");
            newUnit.setFactoryClock("2.5");
            newUnit.setVideoCores("7168");
            newUnit.setPowerDraw("300");
        } else if (newUnit.getSeries().equals("RGX") && newUnit.getModel().equals("600 Super")) {
            newUnit.setVram("16");
            newUnit.setFactoryClock("2.65");
            newUnit.setVideoCores("10240");
            newUnit.setPowerDraw("375");
        } else if (newUnit.getSeries().equals("RGX") && newUnit.getModel().equals("800 Extreme Pi")) {
            newUnit.setVram("24");
            newUnit.setFactoryClock("2.77");
            newUnit.setVideoCores("16384");
            newUnit.setPowerDraw("450");
        }
    }

}
