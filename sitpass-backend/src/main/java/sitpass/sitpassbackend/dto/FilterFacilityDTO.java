package sitpass.sitpassbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterFacilityDTO {
    private String cities;
    private String disciplines;
    private Float minTotalRating;
    private Float maxTotalRating;
    private String workDay;
    private LocalTime from;
    private LocalTime until;
}
