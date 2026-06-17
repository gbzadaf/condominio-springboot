package com.gabrielf.condoaccess.dto;

import jakarta.validation.constraints.NotBlank;

public record VisitorRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Documento é obrigatório")
        String document,

        String phone
) {
}
