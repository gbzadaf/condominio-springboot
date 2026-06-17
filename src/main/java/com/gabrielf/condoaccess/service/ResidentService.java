package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.entity.Resident;
import com.gabrielf.condoaccess.domain.enums.entity.Unit;
import com.gabrielf.condoaccess.domain.enums.repository.ResidentRepository;
import com.gabrielf.condoaccess.domain.enums.repository.UnitRepository;
import com.gabrielf.condoaccess.dto.ResidentRequest;
import com.gabrielf.condoaccess.dto.ResidentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final UnitRepository unitRepository;

    @Transactional
    public ResidentResponse create(ResidentRequest request) {
        if (residentRepository.existsByCpf(request.cpf())) {
            throw  new RuntimeException("Morador com esse CPF %s já existente".formatted(request.cpf()));
        }

        Unit unit = findUnitOrThrow(request.unitId());

        Resident resident = Resident.builder()
                .name(request.name())
                .cpf(request.cpf())
                .phone(request.phone())
                .unit(unit)
                .build();

        Resident saved = residentRepository.save(resident);
        return ResidentResponse.from(saved);

    }

    public ResidentResponse findById(UUID id) {
        Resident resident = findResidentOrThrow(id);
        return ResidentResponse.from(resident);

    }

    public Page<ResidentResponse> findAll(Pageable pageable) {
        return residentRepository.findAll(pageable)
                .map(ResidentResponse::from);

    }

    @Transactional
    public ResidentResponse update(UUID id, ResidentRequest request) {
        Resident resident = findResidentOrThrow(id);
        Unit unit = findUnitOrThrow(request.unitId());

        resident.setName(request.name());
        resident.setPhone(request.phone());
        resident.setUnit(unit);
        // cpf não atualiza — identidade do morador

        Resident updated = residentRepository.save(resident);
        return ResidentResponse.from(updated);

    }

    @Transactional
    public void delete(UUID id) {
        Resident resident = findResidentOrThrow(id);
        resident.softDelete();
        residentRepository.save(resident);

    }



    private Resident findResidentOrThrow(UUID id) {
        return residentRepository.findById(id)
                .filter(resident -> !resident.isDeleted())
                .orElseThrow(() -> new RuntimeException("Morador não encontrado com id: " + id));
    }

    private Unit findUnitOrThrow(UUID id) {
        return unitRepository.findById(id)
                .filter(unit -> !unit.isDeleted())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada com id: " + id));

    }


}
