package com.gabrielf.condoaccess.domain.enums.repository;

import com.gabrielf.condoaccess.domain.enums.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitorRepository extends JpaRepository<Visitor, UUID> {

    Optional<Visitor> findByDocument(String document);
    boolean existsByDocument(String document);


}
