package sitpass.sitpassbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.PeakResultDTO;
import sitpass.sitpassbackend.service.AnalyticsService;
import sitpass.sitpassbackend.service.ManagesService;

import java.time.LocalDateTime;

import static sitpass.sitpassbackend.config.SecurityConfiguration.ROLE_ADMIN;
import static sitpass.sitpassbackend.service.impl.ManagesServiceImpl.facilityString;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities/{facilityId}/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ManagesService managesService;

    @GetMapping("/users")
    public ResponseEntity<Integer> findUsersCountByLevel(@PathVariable Long facilityId, @RequestParam String level,
                                                         @RequestParam(required = false) LocalDateTime dateFrom,
                                                         @RequestParam(required = false) LocalDateTime dateTo)  {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(role.equals(ROLE_ADMIN)) {
            return ResponseEntity.ok(analyticsService.findUsersCount(facilityId, level, dateFrom, dateTo));
        } else {
            Boolean isManager = managesService.isManager(email, facilityId, facilityString);
            if(isManager) {
                return ResponseEntity.ok(analyticsService.findUsersCount(facilityId, level, dateFrom, dateTo));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @GetMapping("/reviews")
    public ResponseEntity<Integer> findReviewsCountByLevel(@PathVariable Long facilityId, @RequestParam String level,
                                                           @RequestParam(required = false) LocalDateTime dateFrom,
                                                           @RequestParam(required = false) LocalDateTime dateTo) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(role.equals(ROLE_ADMIN)) {
            return ResponseEntity.ok(analyticsService.findReviewsCount(facilityId, level, dateFrom, dateTo));
        } else {
            Boolean isManager = managesService.isManager(email, facilityId, facilityString);
            if(isManager) {
                return ResponseEntity.ok(analyticsService.findReviewsCount(facilityId, level, dateFrom, dateTo));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @GetMapping("/peak-hours")
    public ResponseEntity<PeakResultDTO> findPeakHours(@PathVariable Long facilityId, @RequestParam String period,
                                                       @RequestParam(required = false) LocalDateTime dateFrom,
                                                       @RequestParam(required = false) LocalDateTime dateTo) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if(role.equals(ROLE_ADMIN)) {
            PeakResultDTO peakResults = analyticsService.findPeakHours(facilityId, period, dateFrom, dateTo);
            return ResponseEntity.ok(peakResults);
        } else {
            Boolean isManager = managesService.isManager(email, facilityId, facilityString);
            if(isManager) {
                PeakResultDTO peakResults = analyticsService.findPeakHours(facilityId, period, dateFrom, dateTo);
                return ResponseEntity.ok(peakResults);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }
}
