package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisciplineDTO {

    private Long id;
    @NotBlank
    private String name;
    private Facility facility;
    private Boolean isDeleted;

    public static DisciplineDTO convertToDto(Discipline discipline) {
        return DisciplineDTO.builder()
                .id(discipline.getId())
                .name(discipline.getName())
                .facility(discipline.getFacility())
                .isDeleted(discipline.getIsDeleted())
                .build();
    }

    public Discipline convertToModel() {
        return Discipline.builder()
                .id(getId())
                .name(getName())
                .facility(getFacility())
                .isDeleted(getIsDeleted())
                .build();
    }

}
