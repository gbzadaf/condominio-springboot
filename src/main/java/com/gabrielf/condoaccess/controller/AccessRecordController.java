package com.gabrielf.condoaccess.controller;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.dto.AccessExitRequest;
import com.gabrielf.condoaccess.dto.AccessRecordRequest;
import com.gabrielf.condoaccess.dto.AccessRecordResponse;
import com.gabrielf.condoaccess.service.AccessRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/access-records")
@RequiredArgsConstructor
public class AccessRecordController {

    private final AccessRecordService accessRecordService;

    @PostMapping
    public ResponseEntity<AccessRecordResponse> registerEntry(@RequestBody @Valid AccessRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accessRecordService.registerEntry(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessRecordResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AccessRecordResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(accessRecordService.findAll(pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AccessRecordResponse>> findByStatus(
            @PathVariable AccessStatus status, Pageable pageable) {
        return ResponseEntity.ok(accessRecordService.findByStatus(status, pageable));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<AccessRecordResponse> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.approveManually(id));
    }

    @PatchMapping("/{id}/deny")
    public ResponseEntity<AccessRecordResponse> deny(@PathVariable UUID id) {
        return ResponseEntity.ok(accessRecordService.deny(id));
    }

    @PatchMapping("/{id}/exit")
    public ResponseEntity<AccessRecordResponse> registerExit(@PathVariable UUID id,
                                                             @RequestBody AccessExitRequest request) {
        return ResponseEntity.ok(accessRecordService.registerExit(id, request));

    }
}
