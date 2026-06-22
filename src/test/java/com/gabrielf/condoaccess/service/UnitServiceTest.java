package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.entity.Unit;
import com.gabrielf.condoaccess.domain.enums.repository.UnitRepository;
import com.gabrielf.condoaccess.dto.UnitRequest;
import com.gabrielf.condoaccess.dto.UnitResponse;
import com.gabrielf.condoaccess.exception.DuplicateResourceException;
import com.gabrielf.condoaccess.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitService unitService;

    @Test
    void shouldCreateUnitSuccessfully() {
        UnitRequest request = new UnitRequest("A", "101", 1);

        when(unitRepository.existsByBlockAndNumber("A", "101")).thenReturn(false);
        when(unitRepository.save(any(Unit.class))).thenAnswer(invocation -> {
            Unit unit = invocation.getArgument(0);
            unit.setId(UUID.randomUUID());
            return unit;
        });

        UnitResponse response = unitService.create(request);

        assertThat(response.block()).isEqualTo("A");
        assertThat(response.number()).isEqualTo("101");
        assertThat(response.floor()).isEqualTo(1);
        verify(unitRepository).save(any(Unit.class));
    }

    @Test
    void shouldThrowExceptionWhenUnitAlreadyExists() {
        UnitRequest request = new UnitRequest("A", "101", 1);

        when(unitRepository.existsByBlockAndNumber("A", "101")).thenReturn(true);

        assertThatThrownBy(() -> unitService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("já existe");

        verify(unitRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUnitNotFound() {
        UUID id = UUID.randomUUID();
        when(unitRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> unitService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("não encontrada");
    }

    @Test
    void shouldNotReturnDeletedUnit() {
        UUID id = UUID.randomUUID();
        Unit deletedUnit = Unit.builder().id(id).block("A").number("101").floor(1).build();
        deletedUnit.softDelete();

        when(unitRepository.findById(id)).thenReturn(Optional.of(deletedUnit));

        assertThatThrownBy(() -> unitService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

}
