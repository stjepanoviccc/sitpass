package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Manages;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagesDTO {

    private Long id;
    private User user;
    private Facility facility;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private Boolean isDeleted;

    public static ManagesDTO convertToDto(Manages manages) {
        return ManagesDTO.builder()
                .id(manages.getId())
                .user(manages.getUser())
                .facility(manages.getFacility())
                .startDate(manages.getStartDate())
                .endDate(manages.getEndDate())
                .isDeleted(manages.getIsDeleted())
                .build();
    }

    public Manages convertToModel() {
        return Manages.builder()
                .id(getId())
                .user(getUser())
                .facility(getFacility())
                .startDate(getStartDate())
                .endDate(getEndDate())
                .isDeleted(getIsDeleted())
                .build();
    }

}
