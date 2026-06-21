package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.ResidentRequest;
import com.gabrielf.condoaccess.dto.ResidentResponse;
import com.gabrielf.condoaccess.service.ResidentService;
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
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
@Tag(name = "Residents", description = "Gerenciamento de moradores do condomínio")
public class ResidentController {

    private final ResidentService residentService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cadastra um novo morador", description = "Apenas ADMIN ou MANAGER podem cadastrar moradores")
    public ResponseEntity<ResidentResponse> create(@RequestBody @Valid ResidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(residentService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um morador por ID")
    public ResponseEntity<ResidentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(residentService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os moradores", description = "Retorna uma lista paginada de moradores")
    public ResponseEntity<Page<ResidentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(residentService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Atualiza dados de um morador",
            description = "Apenas ADMIN ou MANAGER podem atualizar moradores")
    public ResponseEntity<ResidentResponse> update(@PathVariable UUID id, @RequestBody @Valid ResidentRequest request) {
        return ResponseEntity.ok(residentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Remove um morador (soft delete)",
            description = "Apenas ADMIN ou MANAGER podem remover moradores")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        residentService.delete(id);
        return ResponseEntity.noContent().build();


    }
}
