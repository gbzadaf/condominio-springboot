package com.gabrielf.condoaccess.domain.enums.repository;

import com.gabrielf.condoaccess.domain.enums.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnitRepository extends JpaRepository<Unit, UUID> {

    Optional<Unit> findByBlockAndNumber(String block, String number);
    boolean existsByBlockAndNumber(String block, String number);


}
