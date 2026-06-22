package com.gabrielf.condoaccess.service;

import com.gabrielf.condoaccess.domain.enums.AccessStatus;
import com.gabrielf.condoaccess.domain.enums.UserRole;
import com.gabrielf.condoaccess.domain.enums.entity.*;
import com.gabrielf.condoaccess.domain.enums.repository.*;
import com.gabrielf.condoaccess.dto.AccessExitRequest;
import com.gabrielf.condoaccess.dto.AccessRecordRequest;
import com.gabrielf.condoaccess.dto.AccessRecordResponse;
import com.gabrielf.condoaccess.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccessRecordServiceTest {

    @Mock
    private AccessRecordRepository accessRecordRepository;
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private UnitRepository unitRepository;
    @Mock
    private ResidentRepository residentRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccessRecordService accessRecordService;

    private Visitor visitor;
    private Unit unit;
    private User gatekeeper;
    private Resident resident;

    @BeforeEach
    void setUp() {
        visitor = Visitor.builder().id(UUID.randomUUID()).name("João Visitante").document("12345678900").build();
        unit = Unit.builder().id(UUID.randomUUID()).block("A").number("101").floor(1).build();
        gatekeeper = User.builder().id(UUID.randomUUID()).name("Porteiro").role(UserRole.GATEKEEPER).build();
        resident = Resident.builder().id(UUID.randomUUID()).name("Morador").unit(unit).build();
    }

    @Test
    void shouldRegisterEntryAsAuthorizedWhenResidentPreAuthorizes() {
        AccessRecordRequest request = new AccessRecordRequest(
                visitor.getId(), unit.getId(), resident.getId(), gatekeeper.getId(), "Visita agendada"
        );

        when(visitorRepository.findById(visitor.getId())).thenReturn(Optional.of(visitor));
        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));
        when(residentRepository.findById(resident.getId())).thenReturn(Optional.of(resident));
        when(userRepository.findById(gatekeeper.getId())).thenReturn(Optional.of(gatekeeper));
        when(accessRecordRepository.save(any(AccessRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessRecordResponse response = accessRecordService.registerEntry(request);

        assertThat(response.status()).isEqualTo(AccessStatus.AUTHORIZED);
    }

    @Test
    void shouldRegisterEntryAsPendingWhenNoPreAuthorization() {
        AccessRecordRequest request = new AccessRecordRequest(
                visitor.getId(), unit.getId(), null, gatekeeper.getId(), "Sem autorização prévia"
        );

        when(visitorRepository.findById(visitor.getId())).thenReturn(Optional.of(visitor));
        when(unitRepository.findById(unit.getId())).thenReturn(Optional.of(unit));
        when(userRepository.findById(gatekeeper.getId())).thenReturn(Optional.of(gatekeeper));
        when(accessRecordRepository.save(any(AccessRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessRecordResponse response = accessRecordService.registerEntry(request);

        assertThat(response.status()).isEqualTo(AccessStatus.PENDING);
        verify(residentRepository, never()).findById(any());
    }

    @Test
    void shouldApprovePendingAccessRecord() {
        UUID id = UUID.randomUUID();
        AccessRecord pendingRecord = AccessRecord.builder()
                .id(id).visitor(visitor).unit(unit).gatekeeper(gatekeeper)
                .status(AccessStatus.PENDING).entryTime(LocalDateTime.now())
                .build();

        when(accessRecordRepository.findById(id)).thenReturn(Optional.of(pendingRecord));
        when(accessRecordRepository.save(any(AccessRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessRecordResponse response = accessRecordService.approveManually(id);

        assertThat(response.status()).isEqualTo(AccessStatus.AUTHORIZED);
    }

    @Test
    void shouldNotApproveAlreadyAuthorizedRecord() {
        UUID id = UUID.randomUUID();
        AccessRecord authorizedRecord = AccessRecord.builder()
                .id(id).visitor(visitor).unit(unit).gatekeeper(gatekeeper)
                .status(AccessStatus.AUTHORIZED).entryTime(LocalDateTime.now())
                .build();

        when(accessRecordRepository.findById(id)).thenReturn(Optional.of(authorizedRecord));

        assertThatThrownBy(() -> accessRecordService.approveManually(id))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Apenas acessos em espera");

        verify(accessRecordRepository, never()).save(any());
    }

    @Test
    void shouldRegisterExitForAuthorizedRecord() {
        UUID id = UUID.randomUUID();
        AccessRecord authorizedRecord = AccessRecord.builder()
                .id(id).visitor(visitor).unit(unit).gatekeeper(gatekeeper)
                .status(AccessStatus.AUTHORIZED).entryTime(LocalDateTime.now())
                .build();

        when(accessRecordRepository.findById(id)).thenReturn(Optional.of(authorizedRecord));
        when(accessRecordRepository.save(any(AccessRecord.class))).thenAnswer(inv -> inv.getArgument(0));

        AccessRecordResponse response = accessRecordService.registerExit(id, new AccessExitRequest(null));

        assertThat(response.status()).isEqualTo(AccessStatus.COMPLETED);
        assertThat(response.exitTime()).isNotNull();
    }

    @Test
    void shouldNotRegisterExitForPendingRecord() {
        UUID id = UUID.randomUUID();
        AccessRecord pendingRecord = AccessRecord.builder()
                .id(id).visitor(visitor).unit(unit).gatekeeper(gatekeeper)
                .status(AccessStatus.PENDING).entryTime(LocalDateTime.now())
                .build();

        when(accessRecordRepository.findById(id)).thenReturn(Optional.of(pendingRecord));

        assertThatThrownBy(() -> accessRecordService.registerExit(id, new AccessExitRequest(null)))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Apenas acessos autorizados");
    }
}
