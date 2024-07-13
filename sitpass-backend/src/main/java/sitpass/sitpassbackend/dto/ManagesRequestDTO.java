package sitpass.sitpassbackend.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ManagesRequestDTO {
    private String name;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;

}