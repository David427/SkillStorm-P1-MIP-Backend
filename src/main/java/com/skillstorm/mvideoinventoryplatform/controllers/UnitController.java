package com.skillstorm.mvideoinventoryplatform.controllers;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.exceptions.UnitNotFoundException;
import com.skillstorm.mvideoinventoryplatform.services.UnitService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/units")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class UnitController {

    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    // Error handling is done using ExceptionHandlerAdvice
    @PostMapping()
    public ResponseEntity<UnitDto> createUnit(@RequestBody UnitDto unitDto) throws Exception {
        // We need to get the nested warehouse JSON from the frontend. Ideally, it's passed in as just the id code
        // We can use to it find it the warehouse in the db, then add it to the Unit in the service layer
        UnitDto responseUnit = unitService.create(unitDto);
        return new ResponseEntity<>(responseUnit, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UnitDto>> getAllUnits() {
        List<UnitDto> foundUnits = unitService.findAll();

        if (!foundUnits.isEmpty()) {
            return new ResponseEntity<>(foundUnits, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/location")
    public ResponseEntity<List<UnitDto>> getAllUnitsByWarehouse(@RequestParam("id") String idCode) {
        List<UnitDto> foundUnits = unitService.findAllByWarehouse(idCode);

        if (!foundUnits.isEmpty()) {
            return new ResponseEntity<>(foundUnits, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitDto> getUnit(@PathVariable("id") Long id) throws UnitNotFoundException {
        return new ResponseEntity<>(unitService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnitDto> fullUpdateUnit(@PathVariable("id") Long id, @RequestBody UnitDto unitDto) throws Exception {
        unitDto.setId(id);
        return new ResponseEntity<>(unitService.fullUpdate(unitDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UnitDto> partialUpdateUnit(@PathVariable("id") Long id, @RequestBody UnitDto unitDto) throws UnitNotFoundException {
        unitDto.setId(id);
        return new ResponseEntity<>(unitService.partialUpdate(unitDto), HttpStatus.OK);
    }

    @PatchMapping("/transfer/{id}")
    public ResponseEntity<UnitDto> transferUnit(@PathVariable("id") Long id, @RequestParam("idCode") String idCode) throws Exception {
        return new ResponseEntity<>(unitService.transferUnit(id, idCode), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUnit(@PathVariable("id") Long id) throws UnitNotFoundException {
        unitService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
