package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.UserRequest;
import com.gabrielf.condoaccess.dto.UserResponse;
import com.gabrielf.condoaccess.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Gerenciamento de usuários do sistema (administradores, síndicos, porteiros)")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cria um novo usuário", description = "Apenas ADMIN pode criar usuários por essa rota")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário por ID")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista paginada de usuários")
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualiza dados de um usuário", description = "Apenas ADMIN pode atualizar usuários")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody @Valid UserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove um usuário (soft delete)", description = "Apenas ADMIN pode remover usuários")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();


    }
}
