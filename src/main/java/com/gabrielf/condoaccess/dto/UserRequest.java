package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email precisa ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha não pode ter menos que 6 caracteres")
        String password,

        @NotNull(message = "Perfil é obrigatório")
        UserRole role

) {
}
