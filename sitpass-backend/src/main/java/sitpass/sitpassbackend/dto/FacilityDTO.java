package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Discipline;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Image;
import sitpass.sitpassbackend.model.WorkDay;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FacilityDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private LocalDate createdAt;
    @NotBlank
    private String address;
    @NotBlank
    private String city;
    private Double totalRating;
    private Boolean active;
    @NotNull
    private List<WorkDay> workDays;
    private List<Image> images;
    @NotNull
    private List<Discipline> disciplines;
    private Boolean isDeleted;

    public static FacilityDTO convertToDto(Facility facility) {
        return FacilityDTO.builder()
                .id(facility.getId())
                .name(facility.getName())
                .description(facility.getDescription())
                .createdAt(facility.getCreatedAt())
                .address(facility.getAddress())
                .city(facility.getCity())
                .totalRating(facility.getTotalRating())
                .active(facility.getActive())
                .workDays(facility.getWorkDays())
                .images(facility.getImages())
                .disciplines(facility.getDisciplines())
                .isDeleted(facility.getIsDeleted())
                .build();
    }

    public Facility convertToModel() {
        return Facility.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .createdAt(getCreatedAt())
                .address(getAddress())
                .city(getCity())
                .totalRating(getTotalRating())
                .active(getActive())
                .workDays(getWorkDays())
                .images(getImages())
                .disciplines(getDisciplines())
                .isDeleted(getIsDeleted())
                .build();
    }

}