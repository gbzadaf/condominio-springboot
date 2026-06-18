package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.dto.ResidentRequest;
import com.gabrielf.condoaccess.dto.ResidentResponse;
import com.gabrielf.condoaccess.service.ResidentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/residents")
@RequiredArgsConstructor
public class ResidentController {

    private final ResidentService residentService;


    @PostMapping
    public ResponseEntity<ResidentResponse> create(@RequestBody @Valid ResidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(residentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(residentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ResidentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(residentService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResidentResponse> update(@PathVariable UUID id, @RequestBody @Valid ResidentRequest request) {
        return ResponseEntity.ok(residentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        residentService.delete(id);
        return ResponseEntity.noContent().build();


    }
}
