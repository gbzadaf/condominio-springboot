package com.gabrielf.condoaccess.domain.enums.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String block;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private Integer floor;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<Resident> residents = new ArrayList<>();

}
