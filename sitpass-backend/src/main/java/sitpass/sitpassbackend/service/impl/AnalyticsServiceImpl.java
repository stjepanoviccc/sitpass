package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.PeakResultDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.repository.ReviewRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.AnalyticsService;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    private final String DAY = "DAY";
    private final String WEEK = "WEEK";
    private final String MONTH = "MONTH";
    private final String YEAR = "YEAR";
    private final String asc = "asc";
    private final String desc = "desc";
    private final String errorMsg = "Something went wrong.";

    @Override
    public Integer findUsersCount(Long facilityId, String level, LocalDateTime dateFrom, LocalDateTime dateTo) {
        if(dateFrom != null && dateTo != null) {
            return userRepository.findUsersCountByCustomRange(facilityId, dateFrom, dateTo);
        }
        else if(level.equals(WEEK) || level.equals(MONTH) || level.equals(YEAR)) {
            return userRepository.findUsersCount(facilityId, level);
        } else {
            throw new BadRequestException(errorMsg);
        }
    }

    @Override
    public Integer findReviewsCount(Long facilityId, String level, LocalDateTime dateFrom, LocalDateTime dateTo) {
        if(dateFrom != null && dateTo != null) {
            return reviewRepository.findReviewsCountByCustomRange(facilityId, dateFrom, dateTo);
        }
        else if(level.equals(WEEK) || level.equals(MONTH) || level.equals(YEAR)) {
            return reviewRepository.findReviewsCount(facilityId, level);
        } else {
            throw new BadRequestException(errorMsg);
        }
    }

    @Override
    public PeakResultDTO findPeakHours(Long facilityId, String period, LocalDateTime dateFrom, LocalDateTime dateTo) {
        Map<Integer, Integer> bestHour;
        Map<Integer, Integer> worstHour;

        if(dateFrom != null && dateTo != null) {
            bestHour = userRepository.findPeakHoursByCustomRange(facilityId, desc, dateFrom, dateTo);
            worstHour = userRepository.findPeakHoursByCustomRange(facilityId, asc, dateFrom, dateTo);
        }
        else if(period.equals(DAY) || period.equals(WEEK) || period.equals(MONTH)) {
            bestHour = userRepository.findPeakHours(facilityId, period, desc);
            worstHour = userRepository.findPeakHours(facilityId, period, asc);
        } else {
            throw new BadRequestException(errorMsg);
        }

        return new PeakResultDTO(bestHour, worstHour);
    }
}
