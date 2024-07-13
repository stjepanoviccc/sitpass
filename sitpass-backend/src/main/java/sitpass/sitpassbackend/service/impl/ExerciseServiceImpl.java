package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.ExerciseDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Exercise;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.model.WorkDay;
import sitpass.sitpassbackend.repository.ExerciseRepository;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.ExerciseService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static sitpass.sitpassbackend.dto.ExerciseDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final Logger logger = LogManager.getLogger(ExerciseServiceImpl.class);
    private final ExerciseRepository exerciseRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Override
    public List<ExerciseDTO> findAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {return new NotFoundException(String.format("User with email %s not found.", email));  });
        List<Exercise> exercises = exerciseRepository.findAllByUser(user);
        return exercises.stream()
                .map(ExerciseDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExerciseDTO> findAllByFacilityAndUser(Long facilityId, String email) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityId)));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));

        List<Exercise> exercises = exerciseRepository.findAllByFacilityAndUser(facility, user);

        LocalDateTime now = LocalDateTime.now();

        List<ExerciseDTO> filteredExercises = exercises.stream()
                .filter(exercise -> exercise.getFrom().isBefore(now) && exercise.getUntil().isBefore(now))
                .map(ExerciseDTO::convertToDto)
                .collect(Collectors.toList());

        return filteredExercises;
    }

    @Override
    public ExerciseDTO create(ExerciseDTO exerciseDTO, Long facilityId, String email) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityId)));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));

        if (!facility.getActive()) {
            throw new BadRequestException("Facility isn't active.");
        }

        validateExerciseTime(exerciseDTO, facility);

        exerciseDTO.setFacility(facility);
        exerciseDTO.setUser(user);

        ExerciseDTO savedExercise = convertToDto(exerciseRepository.save(exerciseDTO.convertToModel()));
        return savedExercise;
    }

    @Override
    public void validateExerciseTime(ExerciseDTO exerciseDTO, Facility facility) {
        LocalDateTime from = exerciseDTO.getFrom();
        LocalDateTime until = exerciseDTO.getUntil();

        if (from.isAfter(until)) {
            throw new BadRequestException("The 'from' time must be before the 'until' time.");
        }

        DayOfWeek dayOfWeek = from.getDayOfWeek();
        List<WorkDay> workDays = facility.getWorkDays();

        WorkDay workDay = workDays.stream()
                .filter(wd -> wd.getDay().toString().toUpperCase().equals(dayOfWeek.toString().toUpperCase()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Facility is closed on " + dayOfWeek));

        LocalTime startTime = workDay.getFrom();
        LocalTime endTime = workDay.getUntil();

        if (from.toLocalTime().isBefore(startTime) || until.toLocalTime().isAfter(endTime)) {
            throw new BadRequestException("Exercise time is outside the facility's working hours on " + dayOfWeek);
        }
    }
}
