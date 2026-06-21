package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.VisitorRequest;
import com.gabrielf.condoaccess.dto.VisitorResponse;
import com.gabrielf.condoaccess.service.VisitorService;
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
public class VisitorController {

    private final VisitorService visitorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'GATEKEEPER')")
    public ResponseEntity<VisitorResponse> create(@RequestBody @Valid VisitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(visitorService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(visitorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<VisitorResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(visitorService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'GATEKEEPER')")
    public ResponseEntity<VisitorResponse> update(@PathVariable UUID id, @RequestBody @Valid VisitorRequest request) {
        return ResponseEntity.ok(visitorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        visitorService.delete(id);
        return ResponseEntity.noContent().build();

    }

}
