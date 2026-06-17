package com.gabrielf.condoaccess.domain.enums.repository;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.domain.enums.entity.AccessRecord;
import com.gabrielf.condoaccess.domain.enums.entity.Visitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccessRecordRepository extends JpaRepository<AccessRecord, UUID> {

    List<AccessRecord> findAllByVisitorId(UUID visitorId);
    List<AccessRecord> findAllByUnitId(UUID unitId);
    Page<AccessRecord> findAllByStatus(AccessStatus status, Pageable pageable);
    List<AccessRecord> findAllByEntryTimeBetween(LocalDateTime start, LocalDateTime end);

}



