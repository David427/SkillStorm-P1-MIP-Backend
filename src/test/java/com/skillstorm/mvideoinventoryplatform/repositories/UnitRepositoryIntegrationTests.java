package com.skillstorm.mvideoinventoryplatform.repositories;

import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;
import com.skillstorm.mvideoinventoryplatform.utils.TestDataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UnitRepositoryIntegrationTests {

    /*
     * NOTE: I think I only need to test custom queries or session methods; the basic save(), findById(), etc.
     * should be well-tested by Spring Data JPA
     */

    private final WarehouseRepository warehouseRepository;

    private final UnitRepository unitRepository;

    @Autowired
    public UnitRepositoryIntegrationTests(WarehouseRepository warehouseRepository, UnitRepository unitRepository) {
        this.warehouseRepository = warehouseRepository;
        this.unitRepository = unitRepository;
    }

    @Test
    public void testThatCountBySeriesAndModelFunctions() {
        Warehouse warehouse1 = TestDataUtil.createTestWarehouse1();
        Warehouse warehouse2 = TestDataUtil.createTestWarehouse2();
        warehouseRepository.save(warehouse1);
        warehouseRepository.save(warehouse2);

        List<Unit> units = new ArrayList<>();
        units.add(TestDataUtil.createTestUnit1(warehouse1));
        units.add(TestDataUtil.createTestUnit2(warehouse2));
        units.add(TestDataUtil.createTestUnit3(warehouse1));
        units.add(TestDataUtil.createTestUnit4(warehouse1));
        units.add(TestDataUtil.createTestUnit5(warehouse2));
        units.add(TestDataUtil.createTestUnit6(warehouse1));

        for (Unit unit : units) {
            unitRepository.save(unit);
        }

        Integer model1Count = unitRepository.countByModelAndWarehouse("400", "BOS-WH-01-TEST");
        Integer model2Count = unitRepository.countByModelAndWarehouse("600 Super", "BOS-WH-01-TEST");
        Integer model3Count = unitRepository.countByModelAndWarehouse("800 Extreme Pi", "BOS-WH-01-TEST");

        // Test data: 1 RGX 400 in WH1, 1 RGX 600 Super in WH1, 2 RGX 800 Extreme Pis in WH2
        // Other GpuUnits are in WH2
        assertThat(model1Count).isEqualTo(1);
        assertThat(model2Count).isEqualTo(1);
        assertThat(model3Count).isEqualTo(2);
    }

}
