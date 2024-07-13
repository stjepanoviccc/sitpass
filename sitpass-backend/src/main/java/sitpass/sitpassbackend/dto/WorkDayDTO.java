package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.WorkDay;
import sitpass.sitpassbackend.model.enums.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkDayDTO {

    private Long id;
    private LocalDate validFrom;
    @NotNull
    private DayOfWeek day;
    @NotNull
    private LocalTime from;
    @NotNull
    private LocalTime until;
    private Facility facility;
    private Boolean isDeleted;

    public static WorkDayDTO convertToDto(WorkDay workDay) {
        return WorkDayDTO.builder()
                .id(workDay.getId())
                .validFrom(workDay.getValidFrom())
                .day(workDay.getDay())
                .from(workDay.getFrom())
                .until(workDay.getUntil())
                .facility(workDay.getFacility())
                .isDeleted(workDay.getIsDeleted())
                .build();
    }

    public WorkDay convertToModel() {
        return WorkDay.builder()
                .id(getId())
                .validFrom(getValidFrom())
                .day(getDay())
                .from(getFrom())
                .until(getUntil())
                .facility(getFacility())
                .isDeleted(getIsDeleted())
                .build();
    }

}
