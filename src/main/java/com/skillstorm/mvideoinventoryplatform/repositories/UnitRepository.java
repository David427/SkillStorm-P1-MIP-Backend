package com.skillstorm.mvideoinventoryplatform.repositories;

import com.skillstorm.mvideoinventoryplatform.domain.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT COUNT(*) FROM Unit u WHERE u.model = ?1 AND u.warehouse.idCode = ?2")
    Integer countByModelAndWarehouse(String model, String idCode);

    List<Unit> findAllByWarehouseIdCode(String idCode);

}
