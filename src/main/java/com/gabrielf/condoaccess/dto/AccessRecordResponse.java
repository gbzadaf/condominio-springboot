package com.gabrielf.condoaccess.dto;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.domain.enums.entity.AccessRecord;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessRecordResponse(

        UUID id,
        VisitorResponse visitor,
        UnitResponse unit,
        ResidentResponse authorizingResident, // pode ser null
        UUID gatekeeperId,
        String gatekeeperName,
        AccessStatus status,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        String notes
) {
    public static AccessRecordResponse from(AccessRecord record) {
        return new AccessRecordResponse(
                record.getId(),
                VisitorResponse.from(record.getVisitor()),
                UnitResponse.from(record.getUnit()),
                record.getAuthorizingResident() != null
                        ? ResidentResponse.from(record.getAuthorizingResident())
                        : null,
                record.getGatekeeper().getId(),
                record.getGatekeeper().getName(),
                record.getStatus(),
                record.getEntryTime(),
                record.getExitTime(),
                record.getNotes()
        );

    }
}
