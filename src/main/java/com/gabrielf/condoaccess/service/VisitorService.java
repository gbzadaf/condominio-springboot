package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.entity.Visitor;
import com.gabrielf.condoaccess.domain.enums.repository.VisitorRepository;
import com.gabrielf.condoaccess.dto.VisitorRequest;
import com.gabrielf.condoaccess.dto.VisitorResponse;
import com.gabrielf.condoaccess.exception.DuplicateResourceException;
import com.gabrielf.condoaccess.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitorService {

    private final VisitorRepository visitorRepository;

    public VisitorResponse create(VisitorRequest request) {
        if (visitorRepository.existsByDocument(request.document())) {
            throw new DuplicateResourceException("Visitante com documento %s já existe".formatted(request.document())
            );
        }

        Visitor visitor = Visitor.builder()
                .name(request.name())
                .document(request.document())
                .phone(request.phone())
                .build();

        Visitor saved = visitorRepository.save(visitor);
        return VisitorResponse.from(saved);

    }

    public VisitorResponse findById(UUID id) {
        Visitor visitor = findVisitorOrThrow(id);
        return VisitorResponse.from(visitor);

    }

    public Page<VisitorResponse> findAll(Pageable pageable) {
        return visitorRepository.findAll(pageable)
                .map(VisitorResponse::from);

    }

    public VisitorResponse update(UUID id, VisitorRequest request) {
        Visitor visitor = findVisitorOrThrow(id);

        visitor.setName(request.name());
        visitor.setPhone(request.phone());

        Visitor updated = visitorRepository.save(visitor);
        return VisitorResponse.from(updated);

    }

    public void delete(UUID id) {
        Visitor visitor = findVisitorOrThrow(id);
        visitor.softDelete();
        visitorRepository.save(visitor);
    }


    private Visitor findVisitorOrThrow(UUID id) {
        return visitorRepository.findById(id)
                .filter(visitor -> !visitor.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Visitante não encontrado com id: " + id));

    }
}
