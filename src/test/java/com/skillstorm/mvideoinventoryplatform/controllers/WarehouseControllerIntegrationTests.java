package com.skillstorm.mvideoinventoryplatform.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class WarehouseControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final WarehouseService warehouseService;

    private final UnitService unitService;

    @Autowired
    public WarehouseControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper, WarehouseService warehouseService, UnitService unitService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.warehouseService = warehouseService;
        this.unitService = unitService;
    }

    @Test
    public void testThatCreateWarehouseReturnsHttp201AndCreatedWarehouse() throws Exception {
        WarehouseDto warehouseDto = TestDataUtil.createTestWarehouseDto1();
        String warehouseDtoCreateJson = objectMapper.writeValueAsString(warehouseDto);

        mockMvc.perform(post("/warehouses").contentType(MediaType.APPLICATION_JSON).content(warehouseDtoCreateJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCode").value(warehouseDto.getIdCode()))
                .andExpect(jsonPath("$.streetAddress").value(warehouseDto.getStreetAddress()))
                .andExpect(jsonPath("$.city").value(warehouseDto.getCity()))
                .andExpect(jsonPath("$.state").value(warehouseDto.getState()))
                .andExpect(jsonPath("$.zipCode").value(warehouseDto.getZipCode()))
                .andExpect(jsonPath("$.squareFt").value(warehouseDto.getSquareFt()))
                .andExpect(jsonPath("$.stock").value(warehouseDto.getStock()))
                .andExpect(jsonPath("$.capacity").value(warehouseDto.getCapacity()));
    }

    @Test
    public void testThatCreateExistentWarehouseReturnsHttp422() throws Exception {
        WarehouseDto existentWarehouse = TestDataUtil.createTestWarehouseDto1();
        warehouseService.create(existentWarehouse);

        WarehouseDto newWarehouse = TestDataUtil.createTestWarehouseDto1();
        String warehouseDtoCreateJson = objectMapper.writeValueAsString(newWarehouse);

        mockMvc.perform(post("/warehouses").contentType(MediaType.APPLICATION_JSON).content(warehouseDtoCreateJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("ERROR: This warehouse already exists."));
    }

    @Test
    public void testThatGetAllWarehousesReturnsHttp302() throws Exception {
        warehouseService.create(TestDataUtil.createTestWarehouseDto1());
        warehouseService.create(TestDataUtil.createTestWarehouseDto2());
        warehouseService.create(TestDataUtil.createTestWarehouseDto3());

        mockMvc.perform(get("/warehouses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }

    @Test
    public void testThatGetOneWarehouseReturnsHttp302() throws Exception {
        WarehouseDto existentWarehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());


        mockMvc.perform(get("/warehouses/" + existentWarehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());
    }

    @Test
    public void testThatGetOneNonExistentWarehouseReturnsHttp404() throws Exception {
        // Creating the DTO but not saving it to the db
        WarehouseDto nonExistentWarehouse = TestDataUtil.createTestWarehouseDto1();

        mockMvc.perform(get("/warehouses/" + nonExistentWarehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ERROR: Warehouse not found."));
    }

    @Test
    public void testThatGetWarehouseStockReturnsHttp200AndCorrectStock() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        warehouse.setStock(0);
        warehouseService.create(warehouse);

        unitService.create(TestDataUtil.createTestUnitDto1(warehouse));
        unitService.create(TestDataUtil.createTestUnitDto2(warehouse));
        unitService.create(TestDataUtil.createTestUnitDto3(warehouse));
        unitService.create(TestDataUtil.createTestUnitDto4(warehouse));
        unitService.create(TestDataUtil.createTestUnitDto5(warehouse));
        unitService.create(TestDataUtil.createTestUnitDto6(warehouse));

        mockMvc.perform(
                get("/warehouses/" + warehouse.getIdCode() + "/stock")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andExpect(
                jsonPath("$[0].stock").value(1)
        ).andExpect(
                jsonPath("$[1].stock").value(2)
        ).andExpect(
                jsonPath("$[2].stock").value(3)
        );
    }

    @Test
    public void testThatFullUpdateWarehouseReturnsHttp200AndUpdatedWarehouse() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());

        WarehouseDto updatedWarehouse = TestDataUtil.createTestWarehouseDto2();
        String warehouseJson = objectMapper.writeValueAsString(updatedWarehouse);

        mockMvc.perform(put("/warehouses/" + warehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON).content(warehouseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCode").value(warehouse.getIdCode()))
                .andExpect(jsonPath("$.streetAddress").value(updatedWarehouse.getStreetAddress()))
                .andExpect(jsonPath("$.city").value(updatedWarehouse.getCity()))
                .andExpect(jsonPath("$.state").value(updatedWarehouse.getState()))
                .andExpect(jsonPath("$.zipCode").value(updatedWarehouse.getZipCode()))
                .andExpect(jsonPath("$.squareFt").value(updatedWarehouse.getSquareFt()))
                .andExpect(jsonPath("$.stock").value(updatedWarehouse.getStock()))
                .andExpect(jsonPath("$.capacity").value(updatedWarehouse.getCapacity()));
    }

    @Test
    public void testThatPartialUpdateWarehouseReturnsHttp200AndUpdatedWarehouse() throws Exception {
        WarehouseDto warehouse = warehouseService.create(TestDataUtil.createTestWarehouseDto1());

        WarehouseDto updatedWarehouse = new WarehouseDto();
        updatedWarehouse.setStreetAddress("555 Testington Ave");
        String warehouseJson = objectMapper.writeValueAsString(updatedWarehouse);

        mockMvc.perform(patch("/warehouses/" + warehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON).content(warehouseJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCode").value(warehouse.getIdCode()))
                .andExpect(jsonPath("$.streetAddress").value("555 Testington Ave"))
                .andExpect(jsonPath("$.city").value(warehouse.getCity()))
                .andExpect(jsonPath("$.state").value(warehouse.getState()))
                .andExpect(jsonPath("$.zipCode").value(warehouse.getZipCode()))
                .andExpect(jsonPath("$.squareFt").value(warehouse.getSquareFt()))
                .andExpect(jsonPath("$.stock").value(warehouse.getStock()))
                .andExpect(jsonPath("$.capacity").value(warehouse.getCapacity()));
    }

    @Test
    public void testThatUpdateNonExistentWarehouseReturnsHttp404() throws Exception {
        WarehouseDto updatedWarehouse = TestDataUtil.createTestWarehouseDto2();
        String warehouseJson = objectMapper.writeValueAsString(updatedWarehouse);

        mockMvc.perform(put("/warehouses/" + updatedWarehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON).content(warehouseJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ERROR: Warehouse not found."));
    }

    @Test
    @Transactional
    public void testThatDeleteWarehouseReturnsHttpStatus204() throws Exception {
        WarehouseDto warehouse = TestDataUtil.createTestWarehouseDto1();
        WarehouseDto savedWarehouse = warehouseService.create(warehouse);

        mockMvc.perform(delete("/warehouses/" + savedWarehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void testThatDeleteNonExistentWarehouseReturnsHttp404() throws Exception {
        WarehouseDto nonExistentWarehouse = TestDataUtil.createTestWarehouseDto2();

        mockMvc.perform(delete("/warehouses/" + nonExistentWarehouse.getIdCode()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("ERROR: Warehouse not found."));
    }

}
