package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Rate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RateDTO {

    private Long id;
    @NotNull
    private Integer equipment;
    @NotNull
    private Integer staff;
    @NotNull
    private Integer hygene;
    @NotNull
    private Integer space;
    private Boolean isDeleted;

    public static RateDTO convertToDto(Rate rate) {
        return RateDTO.builder()
                .id(rate.getId())
                .equipment(rate.getEquipment())
                .staff(rate.getStaff())
                .hygene(rate.getHygene())
                .space(rate.getSpace())
                .isDeleted(rate.getIsDeleted())
                .build();
    }

    public Rate convertToModel() {
        return Rate.builder()
                .id(getId())
                .equipment(getEquipment())
                .staff(getStaff())
                .hygene(getHygene())
                .space(getSpace())
                .isDeleted(getIsDeleted())
                .build();
    }

}
