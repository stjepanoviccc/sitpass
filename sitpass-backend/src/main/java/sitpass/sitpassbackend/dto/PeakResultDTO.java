package sitpass.sitpassbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class PeakResultDTO {
    private Map<Integer, Integer> bestHour;
    private Map<Integer, Integer> worstHour;
}
