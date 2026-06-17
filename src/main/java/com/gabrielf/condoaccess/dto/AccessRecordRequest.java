package com.gabrielf.condoaccess.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AccessRecordRequest(

        @NotNull(message = "Id do Visitante é obrigatório")
        UUID visitorId,

        @NotNull(message = "Id da Unidade é obrigatório")
        UUID unitId,

        UUID authorizingResidentId, // opcional — pode não haver pré-autorização

        @NotNull(message = "Id do Porteiro é obrigatório")
        UUID gatekeeperId,

        String notes
) {
}
