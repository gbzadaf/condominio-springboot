package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.entity.Unit;

import java.time.LocalDateTime;
import java.util.UUID;

public record UnitResponse(
        UUID id,
        String block,
        String number,
        Integer floor,
        LocalDateTime createdAt
) {
    public static UnitResponse from(Unit unit) {
        return new UnitResponse(
                unit.getId(),
                unit.getBlock(),
                unit.getNumber(),
                unit.getFloor(),
                unit.getCreatedAt()

        );
    }
}
