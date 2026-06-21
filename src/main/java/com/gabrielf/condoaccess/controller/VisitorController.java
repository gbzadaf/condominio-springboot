package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.VisitorRequest;
import com.gabrielf.condoaccess.dto.VisitorResponse;
import com.gabrielf.condoaccess.service.VisitorService;
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
@RequestMapping("/api/v1/visitors")
@RequiredArgsConstructor
@Tag(name = "Visitors", description = "Gerenciamento de visitantes do condomínio")
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'GATEKEEPER')")
    @Operation(summary = "Cadastra um novo visitante",
            description = "ADMIN, MANAGER ou GATEKEEPER podem cadastrar visitantes")
    public ResponseEntity<VisitorResponse> create(@RequestBody @Valid VisitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visitorService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um visitante por ID")
    public ResponseEntity<VisitorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(visitorService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os visitantes", description = "Retorna uma lista paginada de visitantes")
    public ResponseEntity<Page<VisitorResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(visitorService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'GATEKEEPER')")
    @Operation(summary = "Atualiza dados de um visitante")
    public ResponseEntity<VisitorResponse> update(@PathVariable UUID id, @RequestBody @Valid VisitorRequest request) {
        return ResponseEntity.ok(visitorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Remove um visitante (soft delete)",
            description = "Apenas ADMIN ou MANAGER podem remover visitantes")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        visitorService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
