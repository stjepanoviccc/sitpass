package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.PeakResultDTO;

import java.time.LocalDateTime;

public interface AnalyticsService {

    Integer findUsersCount(Long facilityId, String level, LocalDateTime dateFrom, LocalDateTime dateTo);
    Integer findReviewsCount(Long facilityId, String level, LocalDateTime dateFrom, LocalDateTime dateTo);
    PeakResultDTO findPeakHours(Long facilityId, String period, LocalDateTime dateFrom, LocalDateTime dateTo);

}
