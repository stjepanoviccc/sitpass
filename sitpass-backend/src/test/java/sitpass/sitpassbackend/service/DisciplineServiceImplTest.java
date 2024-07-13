package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sitpass.sitpassbackend.dto.DisciplineDTO;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.repository.DisciplineRepository;
import sitpass.sitpassbackend.service.impl.DisciplineServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.DisciplineDTO.convertToDto;

@ExtendWith(MockitoExtension.class)
public class DisciplineServiceImplTest {

    private final Discipline discipline = createDiscipline();
    private final Facility facility = new Facility();

    private Discipline createDiscipline() {
        return Discipline.builder()
                .id(1L)
                .name("Running")
                .isDeleted(false)
                .facility(facility)
                .build();
    }

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineServiceImpl disciplineService;

    @Test
    void shouldGetDiscipline_whenGetModel_ifDisciplineExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.of(discipline));

        DisciplineDTO disciplineDTO = convertToDto(disciplineService.getModel(discipline.getId()));

        assertNotNull(disciplineDTO);
        assertEquals(discipline.getId(), disciplineDTO.getId());

        verify(disciplineRepository).findById(discipline.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifDisciplineDoesNotExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(disciplineRepository).findById(discipline.getId());
    }

    @Test
    void shouldCreateDiscipline_whenCreate() {
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);

        DisciplineDTO createdDisciplineDTO = disciplineService.create(convertToDto(discipline));

        assertEquals(createdDisciplineDTO.getId(), discipline.getId());

        verify(disciplineRepository).save(any(Discipline.class));
    }

    @Test
    void shouldUpdateDiscipline_whenUpdate_ifDisciplineExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.of(discipline));
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);

        DisciplineDTO updatedDisciplineDTO = disciplineService.update(facility, convertToDto(discipline));

        assertEquals(updatedDisciplineDTO.getId(), discipline.getId());
        assertEquals(updatedDisciplineDTO.getName(), discipline.getName());

        verify(disciplineRepository).findById(discipline.getId());
        verify(disciplineRepository).save(any(Discipline.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifDisciplineDoesNotExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(disciplineRepository).findById(discipline.getId());
        verify(disciplineRepository, never()).save(any(Discipline.class));
    }

    @Test
    void shouldDeleteDiscipline_whenDelete_ifDisciplineExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.of(discipline));

        disciplineService.delete(discipline.getId());

        verify(disciplineRepository).findById(discipline.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifDisciplineDoesNotExist() {
        when(disciplineRepository.findById(discipline.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(disciplineRepository).findById(discipline.getId());
        verify(disciplineRepository, never()).delete(any(Discipline.class));

    }

    // helping
    void notFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> disciplineService.delete(discipline.getId()));
        assertEquals(String.format("Discipline with id %s not found.", discipline.getId()), exception.getMessage());
    }

}
