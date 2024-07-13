package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    private Long id;
    private User user;
    private Facility facility;
    @NotNull
    private Rate rate;
    private LocalDateTime createdAt;
    @NotNull
    private Integer exerciseCount;
    private Boolean hidden;
    private Comment comment;
    private Boolean isDeleted;

    public static ReviewDTO convertToDto(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .user(review.getUser())
                .facility(review.getFacility())
                .rate(review.getRate())
                .createdAt(review.getCreatedAt())
                .exerciseCount(review.getExerciseCount())
                .hidden(review.getHidden())
                .comment(review.getComment())
                .isDeleted(review.getIsDeleted())
                .build();
    }

    public Review convertToModel() {
        return Review.builder()
                .id(getId())
                .user(getUser())
                .facility(getFacility())
                .rate(getRate())
                .createdAt(getCreatedAt())
                .exerciseCount(getExerciseCount())
                .hidden(getHidden())
                .comment(getComment())
                .isDeleted(getIsDeleted())
                .build();
    }

}
