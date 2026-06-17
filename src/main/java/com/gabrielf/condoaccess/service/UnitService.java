package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.entity.Unit;
import com.gabrielf.condoaccess.domain.enums.repository.UnitRepository;
import com.gabrielf.condoaccess.dto.UnitRequest;
import com.gabrielf.condoaccess.dto.UnitResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;


    @Transactional
    public UnitResponse create(UnitRequest request) {
        if (unitRepository.existsByBlockAndNumber(request.block(), request.number())) {
            throw new RuntimeException("Unidade %s-%s já existe".formatted(request.block(), request.number())
            );
        }

        Unit unit = Unit.builder()
                .block(request.block())
                .number(request.number())
                .floor(request.floor())
                .build();

        Unit saved = unitRepository.save(unit);
        return UnitResponse.from(saved);

    }

    public UnitResponse findById(UUID id) {
        Unit unit = findUnitOrThrow(id);
        return UnitResponse.from(unit);

    }

    public Page<UnitResponse> findAll (Pageable pageable) {
        return unitRepository.findAll(pageable)
                .map(UnitResponse::from);

    }

    @Transactional
    public UnitResponse update(UUID id, UnitRequest request) {
        Unit unit = findUnitOrThrow(id);

        unit.setBlock(request.block());
        unit.setNumber(request.number());
        unit.setFloor(request.floor());

        Unit updated = unitRepository.save(unit);
        return UnitResponse.from(updated);

    }

    @Transactional
    public void delete(UUID id) {
        Unit unit = findUnitOrThrow(id);
        unit.softDelete();
        unitRepository.save(unit);
    }

        private Unit findUnitOrThrow(UUID id) {
            return unitRepository.findById(id)
                    .filter(unit -> !unit.isDeleted())
                    .orElseThrow(() -> new RuntimeException("Unidade não encontrada com id: " + id));
        }



    }



