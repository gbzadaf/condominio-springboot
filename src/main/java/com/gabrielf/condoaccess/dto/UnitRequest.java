package com.gabrielf.condoaccess.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UnitRequest(

        @NotBlank(message = "Bloco é obrigatório")
        String block,

        @NotBlank(message = "Número é obrigatório")
        String number,

        @NotNull(message = "Andar é obrigatório")
        @Positive(message = "Andar precisa ser positivo")
        Integer floor
) {
}
