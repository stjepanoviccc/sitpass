package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Exercise;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExerciseDTO {

    private Long id;
    private User user;
    private Facility facility;
    @NotNull
    private LocalDateTime from;
    @NotNull
    private LocalDateTime until;
    private Boolean isDeleted;

    public static ExerciseDTO convertToDto(Exercise exercise) {
        return ExerciseDTO.builder()
                .id(exercise.getId())
                .user(exercise.getUser())
                .facility(exercise.getFacility())
                .from(exercise.getFrom())
                .until(exercise.getUntil())
                .isDeleted(exercise.getIsDeleted())
                .build();
    }

    public Exercise convertToModel() {
        return Exercise.builder()
                .id(getId())
                .user(getUser())
                .facility(getFacility())
                .from(getFrom())
                .until(getUntil())
                .isDeleted(getIsDeleted())
                .build();
    }

}
