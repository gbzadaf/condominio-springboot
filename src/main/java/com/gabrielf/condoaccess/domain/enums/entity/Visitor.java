package com.gabrielf.condoaccess.domain.enums.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "visitors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visitor extends SoftDeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String document;

    private String phone;

}
