package com.skillstorm.mvideoinventoryplatform.repositories;

import com.skillstorm.mvideoinventoryplatform.domain.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

}
