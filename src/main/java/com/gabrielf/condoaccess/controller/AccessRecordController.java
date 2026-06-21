package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.dto.AccessExitRequest;
import com.gabrielf.condoaccess.dto.AccessRecordRequest;
import com.gabrielf.condoaccess.dto.AccessRecordResponse;
import com.gabrielf.condoaccess.service.AccessRecordService;
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
@RequestMapping("/api/v1/access-records")
@RequiredArgsConstructor
@Tag(name = "Access Records", description = "Controle de entrada e saída de visitantes — a máquina de estados do sistema")
public class AccessRecordController {

    private final AccessRecordService accessRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GATEKEEPER')")
    @Operation(
            summary = "Registra a entrada de um visitante",
            description = "Se houver authorizingResidentId, o acesso já é AUTHORIZED. " +
                    "Caso contrário, fica PENDING até aprovação manual.")
    public ResponseEntity<AccessRecordResponse> registerEntry(@RequestBody @Valid AccessRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accessRecordService.registerEntry(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um registro de acesso por ID")
    public ResponseEntity<AccessRecordResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Lista todos os registros de acesso",
            description = "Retorna uma lista paginada de registros de acesso")
    public ResponseEntity<Page<AccessRecordResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(accessRecordService.findAll(pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lista registros de acesso filtrados por status",
            description = "Status possíveis: PENDING, AUTHORIZED, DENIED, COMPLETED")
    public ResponseEntity<Page<AccessRecordResponse>> findByStatus(
            @PathVariable AccessStatus status, Pageable pageable) {
        return ResponseEntity.ok(accessRecordService.findByStatus(status, pageable));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'GATEKEEPER')")
    @Operation(summary = "Aprova manualmente um acesso pendente",
            description = "Só funciona se o status atual for PENDING")
    public ResponseEntity<AccessRecordResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.approveManually(id));
    }

    @PatchMapping("/{id}/deny")
    @PreAuthorize("hasAnyRole('ADMIN', 'GATEKEEPER')")
    @Operation(summary = "Nega um acesso pendente", description = "Só funciona se o status atual for PENDING")
    public ResponseEntity<AccessRecordResponse> deny(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.deny(id));
    }

    @PatchMapping("/{id}/exit")
    @PreAuthorize("hasAnyRole('ADMIN', 'GATEKEEPER')")
    @Operation(summary = "Registra a saída de um visitante",
            description = "Só funciona se o status atual for AUTHORIZED")
    public ResponseEntity<AccessRecordResponse> registerExit(@PathVariable UUID id,
                                                             @RequestBody AccessExitRequest request) {
        return ResponseEntity.ok(accessRecordService.registerExit(id, request));

    }
}
