package com.gabrielf.condoaccess.domain.enums.entity;

import com.gabrielf.condoaccess.domain.enums.AcessStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "access_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "visitor_id", nullable = false)
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "authorizing_resident_id")
    private Resident authorizingResident;

    @ManyToOne
    @JoinColumn(name = "gatekeeper_id", nullable = false)
    private User gatekeeper;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcessStatus status;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private String notes;

}
