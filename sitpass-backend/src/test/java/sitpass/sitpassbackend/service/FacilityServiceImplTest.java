package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.*;
import sitpass.sitpassbackend.model.enums.DayOfWeek;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.service.impl.FacilityServiceImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.FacilityDTO.convertToDto;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceImplTest {

    private final Facility facility = createFacility(1L);
    private final List<Facility> facilities = Arrays.asList(createFacility(1L), createFacility(2L));
    private final User user = createUser();

    private Facility createFacility(Long id) {
        return Facility.builder()
                .id(id)
                .name("Name")
                .active(true)
                .address("Address")
                .city("City")
                .description("Lorem ipsum dolor sit amet.")
                .disciplines(new ArrayList<>(Collections.singletonList(createDiscipline())))
                .workDays(new ArrayList<>(Collections.singletonList(createWorkDay())))
                .images(new ArrayList<>(Collections.singletonList(createImage())))
                .createdAt(LocalDate.now())
                .isDeleted(false)
                .totalRating(7.5)
                .build();
    }

    private Discipline createDiscipline() {
        return Discipline.builder()
                .id(1L)
                .facility(facility)
                .name("Discipline")
                .isDeleted(false)
                .build();
    }

    private WorkDay createWorkDay() {
        return WorkDay.builder()
                .id(1L)
                .validFrom(LocalDate.MIN)
                .from(LocalTime.MIN)
                .until(LocalTime.MAX)
                .facility(facility)
                .isDeleted(false)
                .day(DayOfWeek.MONDAY)
                .build();
    }

    private Image createImage() {
        return Image.builder()
                .id(1L)
                .facility(facility)
                .user(user)
                .path("path.jpg")
                .isDeleted(false)
                .build();
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("email@gmail.com")
                .build();
    }

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private DisciplineService disciplineService;

    @Mock
    private WorkDayService workDayService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void shouldFindAllFacilities_whenFindAll() {
        when(facilityRepository.findAll()).thenReturn(facilities);

        List<FacilityDTO> facilityDTOList = facilityService.findAll();

        assertNotNull(facilityDTOList);

        verify(facilityRepository).findAll();
        verifyNoMoreInteractions(facilityRepository);
    }

    @Test
    void shouldReturnFacility_whenGetModel_ifFacilityExists() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));

        FacilityDTO facilityDTO = convertToDto(facilityService.getModel(facility.getId()));

        assertNotNull(facilityDTO);
        assertEquals(facility.getId(), facilityDTO.getId());

        verify(facilityRepository).findById(facility.getId());
        verifyNoMoreInteractions(facilityRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        facilityNotFoundByIdAssertion();

        verify(facilityRepository).findById(facility.getId());
        verifyNoMoreInteractions(facilityRepository);
    }

    @Test
    void shouldCreateFacility_whenCreate_ifFacilityDoesNotExist() {
        when(facilityRepository.findByName(facility.getName())).thenReturn(Optional.empty());
        when(facilityRepository.save(any(Facility.class))).thenReturn(facility);

        FacilityDTO createdFacilityDTO = facilityService.create(convertToDto(facility), user.getEmail());

        assertNotNull(createdFacilityDTO);
        assertEquals(facility.getId(), createdFacilityDTO.getId());
        assertEquals(facility.getName(), createdFacilityDTO.getName());

        verify(facilityRepository).save(any(Facility.class));
        verifyNoMoreInteractions(facilityRepository);
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifFacilityExists() {
        when(facilityRepository.findByName(facility.getName())).thenReturn(Optional.of(facility));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                facilityService.create(convertToDto(facility), user.getEmail()));

        assertEquals("Facility with this name already exist.", exception.getMessage());

        verify(facilityRepository, never()).save(any());
    }

    @Test
    void shouldUpdateFacility_whenUpdate_ifFacilityExists() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(facilityRepository.save(any(Facility.class))).thenReturn(facility);

        FacilityDTO updatedFacilityDTO = facilityService.update(convertToDto(facility));

        assertNotNull(updatedFacilityDTO);
        assertEquals(facility.getId(), updatedFacilityDTO.getId());
        assertEquals(facility.getName(), updatedFacilityDTO.getName());

        verify(facilityRepository, times(2)).findById(facility.getId());
        verify(facilityRepository).save(any(Facility.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        facilityNotFoundByIdAssertion();

        verify(facilityRepository).findById(facility.getId());
        verify(facilityRepository, never()).save(any());
    }

    @Test
    void shouldDeleteFacility_whenDelete_ifFacilityExists() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));

        facilityService.delete(facility.getId());

        verify(facilityRepository).findById(facility.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        facilityNotFoundByIdAssertion();

        verify(facilityRepository).findById(facility.getId());
        verify(facilityRepository, never()).delete(any(Facility.class));
    }

    // helping
    private void facilityNotFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> facilityService.getModel(facility.getId()));
        assertEquals(String.format("Facility with id %s not found.", facility.getId()), exception.getMessage());
    }

}
