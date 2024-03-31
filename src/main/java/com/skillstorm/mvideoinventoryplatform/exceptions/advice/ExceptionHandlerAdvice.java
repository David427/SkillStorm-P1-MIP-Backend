package com.skillstorm.mvideoinventoryplatform.exceptions.advice;

import com.skillstorm.mvideoinventoryplatform.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    // Not sure 422 - UNPROCESSABLE ENTITY is the best response code. Maybe 400 - BAD REQUEST is better
    @ExceptionHandler(WarehouseAlreadyExistsException.class)
    public ResponseEntity handleEntityAlreadyExists(WarehouseAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler(UnitAlreadyExistsException.class)
    public ResponseEntity handleEntityAlreadyExists(UnitAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler(WarehouseNotFoundException.class)
    public ResponseEntity handleEntityNotFound(WarehouseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler(UnitNotFoundException.class)
    public ResponseEntity handleEntityNotFound(UnitNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler(WarehouseAtCapacityException.class)
    public ResponseEntity handleWarehouseAtCapacity(WarehouseAtCapacityException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("ERROR: " + e.getMessage());
    }

    @ExceptionHandler(UnitAlreadyInWarehouseException.class)
    public ResponseEntity handleUnitAlreadyInWarehouse(UnitAlreadyInWarehouseException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("ERROR: " + e.getMessage());
    }

}
