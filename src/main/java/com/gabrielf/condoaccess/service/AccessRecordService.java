package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.domain.enums.entity.*;
import com.gabrielf.condoaccess.domain.enums.repository.*;
import com.gabrielf.condoaccess.dto.AccessExitRequest;
import com.gabrielf.condoaccess.dto.AccessRecordRequest;
import com.gabrielf.condoaccess.dto.AccessRecordResponse;
import com.gabrielf.condoaccess.exception.BusinessException;
import com.gabrielf.condoaccess.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessRecordService {

    private final AccessRecordRepository accessRecordRepository;
    private final VisitorRepository visitorRepository;
    private final UnitRepository unitRepository;
    private final ResidentRepository residentRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccessRecordResponse registerEntry(AccessRecordRequest request) {
        Visitor visitor = findVisitorOrThrow(request.visitorId());
        Unit unit = findUnitOrThrow(request.unitId());
        User gatekeeper = findGatekeeperOrThrow(request.gatekeeperId());

        Resident authorizingResident = null;
        AccessStatus status;

        if (request.authorizingResidentId() != null) {
            authorizingResident = findResidentOrThrow(request.authorizingResidentId());
            status = AccessStatus.AUTHORIZED; // morador pré-autorizou
        } else {
            status = AccessStatus.PENDING; // aguardando aprovação manual do porteiro
        }

        AccessRecord accessRecord = AccessRecord.builder()
                .visitor(visitor)
                .unit(unit)
                .authorizingResident(authorizingResident)
                .gatekeeper(gatekeeper)
                .status(status)
                .entryTime(LocalDateTime.now())
                .notes(request.notes())
                .build();

        AccessRecord saved = accessRecordRepository.save(accessRecord);
        return AccessRecordResponse.from(saved);
    }

    @Transactional
    public AccessRecordResponse approveManually(UUID id) {
        AccessRecord accessRecord = findAccessRecordOrThrow(id);

        if (accessRecord.getStatus() != AccessStatus.PENDING) {
            throw new BusinessException("Apenas acessos em espera podem ser aprovados");
        }

        accessRecord.setStatus(AccessStatus.AUTHORIZED);
        AccessRecord updated = accessRecordRepository.save(accessRecord);
        return AccessRecordResponse.from(updated);
    }

    @Transactional
    public AccessRecordResponse deny(UUID id) {
        AccessRecord accessRecord = findAccessRecordOrThrow(id);

        if (accessRecord.getStatus() != AccessStatus.PENDING) {
            throw new BusinessException("Apenas acessos em espera podem ser negados");
        }

        accessRecord.setStatus(AccessStatus.DENIED);
        AccessRecord updated = accessRecordRepository.save(accessRecord);
        return AccessRecordResponse.from(updated);
    }

    @Transactional
    public AccessRecordResponse registerExit(UUID id, AccessExitRequest request) {
        AccessRecord accessRecord = findAccessRecordOrThrow(id);

        if (accessRecord.getStatus() != AccessStatus.AUTHORIZED) {
            throw new BusinessException("Apenas acessos autorizados podem registrar uma saída");
        }

        accessRecord.setExitTime(LocalDateTime.now());
        accessRecord.setStatus(AccessStatus.COMPLETED);

        if (request.notes() != null) {
            accessRecord.setNotes(accessRecord.getNotes() + " | Saída: " + request.notes());
        }

        AccessRecord updated = accessRecordRepository.save(accessRecord);
        return AccessRecordResponse.from(updated);
    }

    public AccessRecordResponse findById(UUID id) {
        return AccessRecordResponse.from(findAccessRecordOrThrow(id));
    }

    public Page<AccessRecordResponse> findAll(Pageable pageable) {
        return accessRecordRepository.findAll(pageable)
                .map(AccessRecordResponse::from);
    }

    public Page<AccessRecordResponse> findByStatus(AccessStatus status, Pageable pageable) {
        return accessRecordRepository.findAllByStatus(status, pageable)
                .map(AccessRecordResponse::from);
    }

    private AccessRecord findAccessRecordOrThrow(UUID id) {
        return accessRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de acesso não encontrado com id: " + id));
    }

    private Visitor findVisitorOrThrow(UUID id) {
        return visitorRepository.findById(id)
                .filter(v -> !v.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Visitante não encontrado com id: " + id));
    }

    private Unit findUnitOrThrow(UUID id) {
        return unitRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada com id: " + id));
    }

    private Resident findResidentOrThrow(UUID id) {
        return residentRepository.findById(id)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Morador não encontrado com id: " + id));
    }

    private User findGatekeeperOrThrow(UUID id) {
        return userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Porteiro não encontrado com id: " + id));
    }

}
