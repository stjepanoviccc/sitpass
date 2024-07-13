package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.ExerciseDTO;
import sitpass.sitpassbackend.model.Facility;

import java.util.List;

public interface ExerciseService {

    List<ExerciseDTO> findAllByUser(String email);
    List<ExerciseDTO> findAllByFacilityAndUser(Long facilityId, String email);
    ExerciseDTO create(ExerciseDTO exerciseDTO, Long facilityId, String email);
    void validateExerciseTime(ExerciseDTO exerciseDTO, Facility facility);
}
