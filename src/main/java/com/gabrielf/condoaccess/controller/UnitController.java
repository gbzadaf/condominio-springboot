package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.UnitRequest;
import com.gabrielf.condoaccess.dto.UnitResponse;
import com.gabrielf.condoaccess.service.UnitService;
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
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
@Tag(name = "Units", description = "Gerenciamento de unidades do condomínio")
public class UnitController {

    private final UnitService unitService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Cria uma nova unidade", description = "Apenas ADMIN ou MANAGER podem criar unidades")
    public ResponseEntity<UnitResponse> create(@RequestBody @Valid UnitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(unitService.create(request));

    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma unidade por ID")
    public ResponseEntity<UnitResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(unitService.findById(id));

    }

    @GetMapping
    @Operation(summary = "Lista todas as unidades", description = "Retorna uma lista paginada de unidades")
    public ResponseEntity<Page<UnitResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(unitService.findAll(pageable));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Atualiza uma unidade existente",
            description = "Apenas ADMIN ou MANAGER podem atualizar unidades")
    public ResponseEntity<UnitResponse> update(@PathVariable UUID id, @RequestBody @Valid UnitRequest request) {
        return ResponseEntity.ok(unitService.update(id, request));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Remove uma unidade (soft delete)",
            description = "Apenas ADMIN ou MANAGER podem remover unidades")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        unitService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
