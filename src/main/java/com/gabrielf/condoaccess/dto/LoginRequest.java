package com.gabrielf.condoaccess.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email precisa ser válido")
        String email,


        @NotBlank(message = "Senha é obrigatória")
        String password

) {
}
