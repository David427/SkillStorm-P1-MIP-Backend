package com.skillstorm.mvideoinventoryplatform.controllers;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseStockDto;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseAlreadyExistsException;
import com.skillstorm.mvideoinventoryplatform.exceptions.WarehouseNotFoundException;
import com.skillstorm.mvideoinventoryplatform.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // Error handling is done using ExceptionHandlerAdvice
    @PostMapping
    public ResponseEntity<WarehouseDto> createWarehouse(@RequestBody WarehouseDto warehouseDto) throws WarehouseAlreadyExistsException {
        return new ResponseEntity<>(warehouseService.create(warehouseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDto>> getAllWarehouses() {
        List<WarehouseDto> foundWarehouses = warehouseService.findAll();

        // This list should never be empty, but just in case...
        if (!foundWarehouses.isEmpty()) {
            return new ResponseEntity<>(foundWarehouses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{idCode}")
    public ResponseEntity<WarehouseDto> getWarehouse(@PathVariable("idCode") String idCode) throws WarehouseNotFoundException {
        return new ResponseEntity<>(warehouseService.findById(idCode), HttpStatus.OK);
    }

    @GetMapping("/{idCode}/stock")
    public ResponseEntity<List<WarehouseStockDto>> getWarehouseStock(@PathVariable("idCode") String idCode) {
        return new ResponseEntity<>(warehouseService.getStock(idCode), HttpStatus.OK);
    }

    @PutMapping("/{idCode}")
    public ResponseEntity<WarehouseDto> fullUpdateWarehouse(@PathVariable("idCode") String idCode, @RequestBody WarehouseDto warehouseDto) throws WarehouseNotFoundException {
        warehouseDto.setIdCode(idCode);
        return new ResponseEntity<>(warehouseService.fullUpdate(warehouseDto), HttpStatus.OK);
    }

    @PatchMapping("/{idCode}")
    public ResponseEntity<WarehouseDto> partialUpdateWarehouse(@PathVariable("idCode") String idCode, @RequestBody WarehouseDto warehouseDto) throws WarehouseNotFoundException {
        warehouseDto.setIdCode(idCode);
        return new ResponseEntity<>(warehouseService.partialUpdate(warehouseDto), HttpStatus.OK);
    }

    @DeleteMapping("/{idCode}")
    public ResponseEntity<Object> deleteWarehouse(@PathVariable("idCode") String idCode) throws WarehouseNotFoundException {
        warehouseService.delete(idCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
