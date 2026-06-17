package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.entity.Resident;

import java.time.LocalDateTime;
import java.util.UUID;

public record ResidentResponse(
        UUID id,
        String name,
        String cpf,
        String phone,
        UnitResponse unit,
        LocalDateTime createdAt
) {
    public static ResidentResponse from(Resident resident) {
        return new ResidentResponse(
                resident.getId(),
                resident.getName(),
                resident.getCpf(),
                resident.getPhone(),
                UnitResponse.from(resident.getUnit()),
                resident.getCreatedAt()
        );

    }

}

//UnitResponse : quem consome a API já recebe os dados da unidade junto, sem precisar de uma segunda chamada.
