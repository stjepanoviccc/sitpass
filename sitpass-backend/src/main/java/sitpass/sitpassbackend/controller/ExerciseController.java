package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.ExerciseDTO;
import sitpass.sitpassbackend.service.ExerciseService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/users")
    public ResponseEntity<List<ExerciseDTO>> findAllByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(exerciseService.findAllByUser(email));
    }

    @GetMapping("/facilities/{facilityId}")
    public ResponseEntity<List<ExerciseDTO>> findAllByFacilityAndUser(@PathVariable Long facilityId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(exerciseService.findAllByFacilityAndUser(facilityId, email));
    }
    @PostMapping("/facilities/{facilityId}")
    public ResponseEntity<ExerciseDTO> createExercise(@Valid @RequestBody ExerciseDTO exerciseDTO, @PathVariable Long facilityId ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(CREATED).body(exerciseService.create(exerciseDTO, facilityId, email));
    }

}
