package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sitpass.sitpassbackend.dto.WorkDayDTO;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.WorkDay;
import sitpass.sitpassbackend.repository.WorkDayRepository;
import sitpass.sitpassbackend.service.impl.WorkDayServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.WorkDayDTO.convertToDto;

@ExtendWith(MockitoExtension.class)
public class WorkDaysServiceImplTest {

    private final WorkDay workDay = createWorkDay();
    private final Facility facility = new Facility();

    private WorkDay createWorkDay() {
        return WorkDay.builder()
                .id(1L)
                .validFrom(LocalDate.MIN)
                .from(LocalTime.MIN)
                .until(LocalTime.MAX)
                .facility(facility)
                .isDeleted(false)
                .build();
    }

    @Mock
    private WorkDayRepository workDayRepository;

    @InjectMocks
    private WorkDayServiceImpl workDayService;

    @Test
    void shouldGetWorkDay_whenGetModel_ifWorkDayExist() {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.of(workDay));

        WorkDayDTO workDayDTO = convertToDto(workDayService.getModel(workDay.getId()));

        assertNotNull(workDayDTO);
        assertEquals(workDay.getId(), workDayDTO.getId());

        verify(workDayRepository).findById(workDay.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifWorkDayDoesNotExist() {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(workDayRepository).findById(workDay.getId());
    }

    @Test
    void shouldCreateWorkDay_whenCreate() {
        when(workDayRepository.save(any(WorkDay.class))).thenReturn(workDay);

        WorkDayDTO createdWorkDayDTO = workDayService.create(convertToDto(workDay));

        assertEquals(createdWorkDayDTO.getId(), workDay.getId());

        verify(workDayRepository).save(any(WorkDay.class));
    }

    @Test
    void shouldUpdateWorkDay_whenUpdate_ifWorkDayExist () {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.of(workDay));
        when(workDayRepository.save(any(WorkDay.class))).thenReturn(workDay);

        WorkDayDTO updatedWorkDayDTO = workDayService.update(workDay.getId(), convertToDto(workDay));

        assertEquals(updatedWorkDayDTO.getId(), workDay.getId());
        assertEquals(updatedWorkDayDTO.getDay(), workDay.getDay());
        assertEquals(updatedWorkDayDTO.getFrom(), workDay.getFrom());
        assertEquals(updatedWorkDayDTO.getUntil(), workDay.getUntil());

        verify(workDayRepository).findById(workDay.getId());
        verify(workDayRepository).save(any(WorkDay.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifWorkDayDoesNotExist() {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(workDayRepository).findById(workDay.getId());
        verify(workDayRepository, never()).save(any(WorkDay.class));
    }

    @Test
    void shouldDeleteWorkDay_whenDelete_ifWorkDayExist () {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.of(workDay));

        workDayService.delete(workDay.getId());

        verify(workDayRepository).deleteById(workDay.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifWorkDayDoesNotExist() {
        when(workDayRepository.findById(workDay.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(workDayRepository).findById(workDay.getId());
        verify(workDayRepository, never()).delete(any(WorkDay.class));
    }

    // helping
    void notFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> workDayService.update(workDay.getId(), convertToDto(workDay)));
        assertEquals(String.format("Work Day with id %s not found.", workDay.getId()), exception.getMessage());
    }
}
