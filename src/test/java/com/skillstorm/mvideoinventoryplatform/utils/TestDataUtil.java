package com.skillstorm.mvideoinventoryplatform.utils;

import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;

public class TestDataUtil {

    // region Unit entities
    public static Unit createTestUnit1(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("400")
                .vram("8")
                .factoryClock("2.6")
                .videoCores("4860")
                .aiCores("840")
                .powerDraw("300")
                .warehouse(warehouse)
                .build();
    }

    public static Unit createTestUnit2(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("600 Super")
                .vram("12")
                .factoryClock("2.9")
                .videoCores("5840")
                .aiCores("960")
                .powerDraw("375")
                .warehouse(warehouse)
                .build();
    }

    public static Unit createTestUnit3(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("600 Super")
                .vram("12")
                .factoryClock("2.9")
                .videoCores("5840")
                .aiCores("960")
                .powerDraw("375")
                .warehouse(warehouse)
                .build();
    }

    public static Unit createTestUnit4(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouse)
                .build();
    }

    public static Unit createTestUnit5(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouse)
                .build();
    }

    public static Unit createTestUnit6(final Warehouse warehouse) {
        return Unit.builder()
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouse)
                .build();
    }
    // endregion

    // region Unit DTOs
    public static UnitDto createTestUnitDto1(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(1L)
                .series("RGX")
                .model("400")
                .vram("8")
                .factoryClock("2.6")
                .videoCores("4860")
                .aiCores("840")
                .powerDraw("300")
                .warehouse(warehouseDto)
                .build();
    }

    public static UnitDto createTestUnitDto2(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(2L)
                .series("RGX")
                .model("600 Super")
                .vram("12")
                .factoryClock("2.9")
                .videoCores("5840")
                .aiCores("960")
                .powerDraw("375")
                .warehouse(warehouseDto)
                .build();
    }

    public static UnitDto createTestUnitDto3(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(3L)
                .series("RGX")
                .model("600 Super")
                .vram("12")
                .factoryClock("2.9")
                .videoCores("5840")
                .aiCores("960")
                .powerDraw("375")
                .warehouse(warehouseDto)
                .build();
    }

    public static UnitDto createTestUnitDto4(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(4L)
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouseDto)
                .build();
    }

    public static UnitDto createTestUnitDto5(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(5L)
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouseDto)
                .build();
    }

    public static UnitDto createTestUnitDto6(final WarehouseDto warehouseDto) {
        return UnitDto.builder()
                .id(6L)
                .series("RGX")
                .model("800 Extreme Pi")
                .vram("16")
                .factoryClock("3.2")
                .videoCores("6600")
                .aiCores("1240")
                .powerDraw("450")
                .warehouse(warehouseDto)
                .build();
    }
    // region

    // region Warehouse entities
    public static Warehouse createTestWarehouse1() {
        return Warehouse.builder()
                .idCode("BOS-WH-01-TEST")
                .streetAddress("4404 Factory St")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02123")
                .squareFt(8000)
                .stock(99)
                .capacity(100)
                .build();
    }

    public static Warehouse createTestWarehouse2() {
        return Warehouse.builder()
                .idCode("BOS-WH-02-TEST")
                .streetAddress("80 Industry Rd")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02127")
                .squareFt(8000)
                .stock(92)
                .capacity(100)
                .build();
    }

    public static Warehouse createTestWarehouse3() {
        return Warehouse.builder()
                .idCode("BOS-WH-03-TEST")
                .streetAddress("921 Brown St")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02130")
                .squareFt(12000)
                .stock(0)
                .capacity(150)
                .build();
    }
    // endregion

    // region Warehouse DTOs
    public static WarehouseDto createTestWarehouseDto1() {
        return WarehouseDto.builder()
                .idCode("BOS-WH-01-TEST")
                .streetAddress("4404 Factory St")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02123")
                .squareFt(8000)
                .stock(99)
                .capacity(100)
                .build();
    }

    public static WarehouseDto createTestWarehouseDto2() {
        return WarehouseDto.builder()
                .idCode("BOS-WH-02-TEST")
                .streetAddress("80 Industry Rd")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02127")
                .squareFt(8000)
                .stock(92)
                .capacity(100)
                .build();
    }

    public static WarehouseDto createTestWarehouseDto3() {
        return WarehouseDto.builder()
                .idCode("BOS-WH-03-TEST")
                .streetAddress("921 Brown St")
                .city("Boston")
                .state(String.valueOf(State.MASSACHUSETTS.getAbbreviation()))
                .zipCode("02130")
                .squareFt(12000)
                .stock(0)
                .capacity(150)
                .build();
    }
    // endregion

}
