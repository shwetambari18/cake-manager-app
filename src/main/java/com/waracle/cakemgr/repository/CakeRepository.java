package com.waracle.cakemgr.repository;

import com.waracle.cakemgr.model.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CakeRepository extends JpaRepository<CakeEntity, Integer> {
}
