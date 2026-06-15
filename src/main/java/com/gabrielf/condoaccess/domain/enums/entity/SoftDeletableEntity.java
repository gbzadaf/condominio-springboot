package com.gabrielf.condoaccess.domain.enums.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class SoftDeletableEntity extends BaseEntity {

    @Column(nullable = false)
    private boolean deleted = false;

    public void softDelete() {
        this.deleted = true;
    }

}


// Estende auditoria e adiciona soft delete
