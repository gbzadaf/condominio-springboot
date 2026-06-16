package com.gabrielf.condoaccess.domain.enums.repository;

import com.gabrielf.condoaccess.domain.enums.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResidentRepository extends JpaRepository<Resident, UUID> {

    Optional<Resident> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
    List<Resident> findAllByUnitId(UUID unitId);


}
