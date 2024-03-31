package com.skillstorm.mvideoinventoryplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillstorm.mvideoinventoryplatform.domain.dtos.UnitDto;
import com.skillstorm.mvideoinventoryplatform.domain.dtos.WarehouseDto;
import com.skillstorm.mvideoinventoryplatform.services.UnitService;
import com.skillstorm.mvideoinventoryplatform.services.WarehouseService;
import com.skillstorm.mvideoinventoryplatform.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UnitControllerIntegrationTests {
    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final UnitService unitService;

    private final WarehouseService warehouseService;

    @Autowired
    public UnitControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, UnitService unitService, WarehouseService warehouseService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.unitService = unitService;
        this.warehouseService = warehouseService;
    }

    @Test
    public void testThatCreateUnitReturnsHttp201AndCreatedUnit() throws Exception {
        // Create & save a warehouse for the service method to find
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        warehouseService.create(warehouse);

        // Only send idCode in the request, as the frontend may not have a complete warehouse object
        WarehouseDto newUnitWarehouse = new WarehouseDto();
        newUnitWarehouse.setIdCode("BOS-WH-01-TEST");

        UnitDto newUnit = TestDataUtil.createTestUnitDto1(newUnitWarehouse);
        String newUnitJson = objectMapper.writeValueAsString(newUnit);

        mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUnitJson)
        ).andExpect(
                status().isCreated()
        ).andExpect(
                jsonPath("$.id").value(newUnit.getId())
        ).andExpect(
                jsonPath("$.series").value(newUnit.getSeries())
        ).andExpect(
                jsonPath("$.model").value(newUnit.getModel())
        ).andExpect(
                jsonPath("$.vram").value(newUnit.getVram())
        ).andExpect(
                jsonPath("$.factoryClock").value(newUnit.getFactoryClock())
        ).andExpect(
                jsonPath("$.videoCores").value(newUnit.getVideoCores())
        ).andExpect(
                jsonPath("$.aiCores").value(newUnit.getAiCores())
        ).andExpect(
                jsonPath("$.powerDraw").value(newUnit.getPowerDraw())
        ).andExpect(
                jsonPath("$[*].idCode").value("BOS-WH-01-TEST")
        );
    }

    @Test
    public void testThatCreateExistentUnitReturnsHttp422() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        warehouseService.create(warehouse);

        WarehouseDto newUnitWarehouse = new WarehouseDto();
        newUnitWarehouse.setIdCode("BOS-WH-01-TEST");

        UnitDto existentUnit = TestDataUtil.createTestUnitDto1(warehouse);
        unitService.create(existentUnit);

        UnitDto newUnit = TestDataUtil.createTestUnitDto1(newUnitWarehouse);
        String newUnitJson = objectMapper.writeValueAsString(newUnit);

        mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUnitJson)
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(
                content().string("ERROR: This unit already exists.")
        );

    }

    @Test
    public void testThatCreateUnitWithoutWarehouseReturnsHttp404() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        warehouseService.create(warehouse);

        WarehouseDto newUnitWarehouse = new WarehouseDto();
        newUnitWarehouse.setIdCode("INVALID-WH-CODE");

        UnitDto newUnit = TestDataUtil.createTestUnitDto1(newUnitWarehouse);
        String newUnitJson = objectMapper.writeValueAsString(newUnit);

        mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUnitJson)
        ).andExpect(
                status().isNotFound()
        ).andExpect(
                content().string("ERROR: Cannot find the warehouse to add unit to.")
        );
    }

    @Test
    public void testThatCreateUnitInFullWarehouseReturnsHttp422() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto3();
        warehouse.setStock(150);
        warehouseService.create(warehouse);

        WarehouseDto newUnitWarehouse = new WarehouseDto();
        newUnitWarehouse.setIdCode("BOS-WH-03-TEST");

        UnitDto newUnit = TestDataUtil.createTestUnitDto1(newUnitWarehouse);
        String newUnitJson = objectMapper.writeValueAsString(newUnit);

        mockMvc.perform(
                post("/units")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUnitJson)
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(
                content().string("ERROR: Cannot add unit to warehouse. Current stock at capacity!")
        );
    }

    @Test
    public void testThatGetAllUnitsReturnsHttp302() throws Exception {
        WarehouseDto warehouse1 = TestDataUtil.createTestWarehouseDto1();
        WarehouseDto warehouse2 = TestDataUtil.createTestWarehouseDto2();
        WarehouseDto warehouse3 = TestDataUtil.createTestWarehouseDto3();
        warehouseService.create(warehouse1);
        warehouseService.create(warehouse2);
        warehouseService.create(warehouse3);

        UnitDto unit1 = TestDataUtil.createTestUnitDto1(warehouse1);
        UnitDto unit2 = TestDataUtil.createTestUnitDto2(warehouse2);
        UnitDto unit3 = TestDataUtil.createTestUnitDto3(warehouse3);
        unitService.create(unit1);
        unitService.create(unit2);
        unitService.create(unit3);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/units")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isFound()
        );
    }

    @Test
    public void testThatGetOneUnitReturnsHttp302() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        UnitDto existentUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));

        mockMvc.perform(
                get("/units/" + existentUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isFound()
        );
    }

    @Test
    public void testThatFullUpdateUnitReturnsHttp200AndUpdatedUnit() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        UnitDto existentUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));

        UnitDto newUnit = TestDataUtil.createTestUnitDto2(warehouse);
        String newUnitJson = objectMapper.writeValueAsString(newUnit);

        mockMvc.perform(
                put("/units/" + existentUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUnitJson)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.id").value(existentUnit.getId())
        ).andExpect(
                jsonPath("$.series").value(newUnit.getSeries())
        ).andExpect(
                jsonPath("$.model").value(newUnit.getModel())
        ).andExpect(
                jsonPath("$.vram").value(newUnit.getVram())
        ).andExpect(
                jsonPath("$.factoryClock").value(newUnit.getFactoryClock())
        ).andExpect(
                jsonPath("$.videoCores").value(newUnit.getVideoCores())
        ).andExpect(
                jsonPath("$.aiCores").value(newUnit.getAiCores())
        ).andExpect(
                jsonPath("$.powerDraw").value(newUnit.getPowerDraw())
        ).andExpect(
                jsonPath("$[*].idCode").value(existentUnit.getWarehouse().getIdCode())
        );
    }

    @Test
    public void testThatPartialUpdateUnitReturnsHttp200AndUpdatedUnit() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        UnitDto existentUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));

        UnitDto updatedUnit = TestDataUtil.createTestUnitDto1(warehouse);
        updatedUnit.setPowerDraw(600);
        String updatedUnitJson = objectMapper.writeValueAsString(updatedUnit);

        mockMvc.perform(
                patch("/units/" + existentUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUnitJson)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$.id").value(existentUnit.getId())
        ).andExpect(
                jsonPath("$.series").value(existentUnit.getSeries())
        ).andExpect(
                jsonPath("$.model").value(existentUnit.getModel())
        ).andExpect(
                jsonPath("$.vram").value(existentUnit.getVram())
        ).andExpect(
                jsonPath("$.factoryClock").value(existentUnit.getFactoryClock())
        ).andExpect(
                jsonPath("$.videoCores").value(existentUnit.getVideoCores())
        ).andExpect(
                jsonPath("$.aiCores").value(existentUnit.getAiCores())
        ).andExpect(
                jsonPath("$.powerDraw").value(updatedUnit.getPowerDraw())
        ).andExpect(
                jsonPath("$[*].idCode").value(existentUnit.getWarehouse().getIdCode())
        );
    }

    @Test
    public void testThatTransferUnitToValidWarehouseReturnsHttp200() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        WarehouseDto newWarehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto2());
        UnitDto existentUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));
        int stock = newWarehouse.getStock();

        mockMvc.perform(
                patch("/units/transfer/" + existentUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCode", newWarehouse.getIdCode())
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$[*].stock").value(stock + 1)
        );
    }

    @Test
    public void testThatTransferUnitToFullWarehouseReturnsHttp422() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        WarehouseDto newWarehouse = TestDataUtil.createTestWarehouseDto2();
        newWarehouse.setStock(newWarehouse.getCapacity());
        warehouseService.create(newWarehouse);
        UnitDto existentUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));

        mockMvc.perform(
                patch("/units/transfer/" + existentUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("idCode", newWarehouse.getIdCode())
        ).andExpect(
                status().isUnprocessableEntity()
        ).andExpect(
                content().string("ERROR: Cannot add unit to warehouse. Current stock at capacity!")
        );
    }

    @Test
    @Transactional
    public void testThatDeleteUnitReturnsHttp204() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        warehouseService.create(warehouse);
        UnitDto savedUnit = unitService.create(TestDataUtil.createTestUnitDto1(warehouse));

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/units/" + savedUnit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    @Transactional
    public void testThatDeleteNonExistentUnitReturnsHttp404() throws Exception {
        UnitDto nonExistentUnit = TestDataUtil.createTestUnitDto1(new WarehouseDto());

        mockMvc.perform(delete("/units/" + nonExistentUnit.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ERROR: Unit not found."));
    }

}
