package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.entity.Visitor;

import java.time.LocalDateTime;
import java.util.UUID;

public record VisitorResponse(

        UUID id,
        String name,
        String document,
        String phone,
        LocalDateTime createdAt
) {

    public static VisitorResponse from(Visitor visitor) {
        return new VisitorResponse(
                visitor.getId(),
                visitor.getName(),
                visitor.getDocument(),
                visitor.getPhone(),
                visitor.getCreatedAt()
        );

    }
}
