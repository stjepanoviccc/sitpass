package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.ReviewDTO;
import sitpass.sitpassbackend.service.ManagesService;
import sitpass.sitpassbackend.service.ReviewService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static sitpass.sitpassbackend.config.SecurityConfiguration.ROLE_ADMIN;
import static sitpass.sitpassbackend.dto.ReviewDTO.convertToDto;
import static sitpass.sitpassbackend.service.impl.ManagesServiceImpl.reviewString;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ManagesService managesService;

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> findById(@PathVariable Long reviewId) {
        return ResponseEntity.ok(convertToDto(reviewService.getModel(reviewId)));
    }

    @GetMapping("/facilities/{facilityId}")
    public ResponseEntity<List<ReviewDTO>> findAllByFacility(@PathVariable Long facilityId) {
        return ResponseEntity.ok(reviewService.findAllByFacility(facilityId));
    }

    @GetMapping("/facilities/{facilityId}/sort")
    public List<ReviewDTO> findAllByFacilityAndSort(
            @PathVariable Long facilityId,
            @RequestParam(value = "sortRating", required = false) String sortRating,
            @RequestParam(value = "sortDate", required = false) String sortDate) {
        return reviewService.findAllByFacilityAndSort(facilityId, sortRating, sortDate);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ReviewDTO>> findAllByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(reviewService.findAllByUser(email));
    }

    @PostMapping("/facilities/{facilityId}")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewDTO reviewDTO, @PathVariable Long facilityId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(CREATED).body(reviewService.create(reviewDTO, facilityId, email));
    }

    @PatchMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> hideReview(@PathVariable Long reviewId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(role.equals(ROLE_ADMIN)) {
            reviewService.hide(reviewId);
        } else {
            Boolean isManager = managesService.isManager(email, reviewId, reviewString);
            if(isManager) {
                reviewService.hide(reviewId);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(role.equals(ROLE_ADMIN)) {
            reviewService.hide(reviewId);
        } else {
            Boolean isManager = managesService.isManager(email, reviewId, reviewString);
            if(isManager) {
                reviewService.hide(reviewId);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
        return ResponseEntity.noContent().build();
    }

}
