package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sitpass.sitpassbackend.dto.ExerciseDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.*;
import sitpass.sitpassbackend.model.enums.DayOfWeek;
import sitpass.sitpassbackend.repository.ExerciseRepository;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.impl.ExerciseServiceImpl;
import sitpass.sitpassbackend.service.impl.FacilityServiceImpl;
import sitpass.sitpassbackend.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.ExerciseDTO.convertToDto;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    private final Exercise exercise = createExercise(50L);
    private final List<Exercise> exercises = Arrays.asList(createExercise(1L), createExercise(2L), createExercise(3L));
    private final User user = new User();
    private final Facility facility = createFacility();

    private Exercise createExercise(Long id) {
        return Exercise.builder()
                .id(id)
                .user(user)
                .facility(facility)
                .from(LocalDateTime.MIN)
                .until(LocalDateTime.MAX)
                .isDeleted(false)
                .build();
    }

    private Facility createFacility() {
        return Facility.builder()
                .id(1L)
                .workDays(Collections.singletonList(createWorkDay()))
                .disciplines(new ArrayList<Discipline>())
                .active(true)
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

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void shouldFindAllByUser_whenFindAllByUser_ifUserExists() {
        user.setEmail("email@gmail.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(exerciseRepository.findAllByUser(user)).thenReturn(exercises);

        List<ExerciseDTO> exerciseDTOList = exerciseService.findAllByUser(user.getEmail());
        assertNotNull(exerciseDTOList);

        verify(userRepository).findByEmail(user.getEmail());
        verify(exerciseRepository).findAllByUser(user);
    }

    @Test
    void shouldThrowNotFoundException_whenFindAllByUser_ifUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getModel(user.getId()));
        assertEquals(String.format("User with id %s not found.", user.getId()), exception.getMessage());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldFindAllByFacilityAndUser_whenFindAllByFacilityAndUser_ifFacilityAndUserExists() {
        user.setEmail("email@gmail.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(exerciseRepository.findAllByFacilityAndUser(facility, user)).thenReturn(exercises);

        List<ExerciseDTO> exerciseDTOList = exerciseService.findAllByFacilityAndUser(facility.getId(), user.getEmail());
        assertNotNull(exerciseDTOList);

        verify(userRepository).findByEmail(user.getEmail());
        verify(facilityRepository).findById(facility.getId());
        verify(exerciseRepository).findAllByFacilityAndUser(facility, user);
    }

    @Test
    void shouldThrowNotFoundException_whenFindAllByFacilityAndUser_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> facilityService.getModel(facility.getId()));
        assertEquals(String.format("Facility with id %s not found.", facility.getId()), exception.getMessage());

        verify(facilityRepository).findById(facility.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenFindAllByFacilityAndUser_ifUserDoesNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getModel(user.getEmail()));
        assertEquals(String.format("User with email %s not found.", user.getEmail()), exception.getMessage());

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void shouldCreateExercise_whenCreateExercise_ifFacilityExistsAndIsActiveAndUserExists() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(exercise);

        ExerciseDTO createdExerciseDTO = exerciseService.create(convertToDto(exercise), facility.getId(), user.getEmail());

        assertEquals(exercise.getFacility(), createdExerciseDTO.getFacility());
        assertEquals(exercise.getUser(), createdExerciseDTO.getUser());

        verify(facilityRepository).findById(facility.getId());
        verify(userRepository).findByEmail(user.getEmail());
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    void shouldThrowBadRequestException_whenCreateExercise_ifFacilityExistsAndIsNotActiveAndUserExists() {
        facility.setActive(false);

        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                exerciseService.create(convertToDto(exercise), facility.getId(), user.getEmail()));
        assertEquals("Facility isn't active.", exception.getMessage());

        verify(exerciseRepository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenCreateExercise_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> facilityService.getModel(facility.getId()));
        assertEquals(String.format("Facility with id %s not found.", facility.getId()), exception.getMessage());

        verify(facilityRepository).findById(facility.getId());
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    void shouldThrowNotFoundException_whenCreateExercise_ifUserDoesNotExist() {
        user.setEmail("e@gmail.com");

        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> exerciseService.create(any(), facility.getId(), user.getEmail()));
        assertEquals(String.format("User with email %s not found.", user.getEmail()), exception.getMessage());

        verify(facilityRepository).findById(facility.getId());
        verify(userRepository).findByEmail(user.getEmail());
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }
}
