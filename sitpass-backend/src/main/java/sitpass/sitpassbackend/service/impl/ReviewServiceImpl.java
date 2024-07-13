package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.RateDTO;
import sitpass.sitpassbackend.dto.ReviewDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Review;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.ManagesRepository;
import sitpass.sitpassbackend.repository.ReviewRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.RateService;
import sitpass.sitpassbackend.service.ReviewService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static sitpass.sitpassbackend.dto.ReviewDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;
    private final RateService rateService;

    private final String CREATE = "CREATE";
    private final String DELETE = "DELETE";
    private final String DEFAULT = "default";

    @Override
    public List<ReviewDTO> findAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));
        List<Review> reviews = reviewRepository.findAllByUserAndNotDeleted(user);
        return reviews.stream()
                .map(ReviewDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> findAllByFacility(Long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityId)));
        List<Review> reviews = reviewRepository.findAllByFacilityAndNotDeleted(facility);
        return reviews.stream()
                .map(ReviewDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDTO> findAllByFacilityAndSort(Long facilityId, String sortRating, String sortDate) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityId)));
        List<Review> reviews = new ArrayList<>();
        if(sortRating.equals(DEFAULT) && sortDate.equals(DEFAULT)) {
            reviews = reviewRepository.findAllByFacilityAndNotDeleted(facility);
        } else {
            if(sortRating.equals(DEFAULT) && !sortDate.equals(DEFAULT)) {
                reviews = reviewRepository.findAllByFacilityAndNotDeletedAndSortByDate(facility, sortDate);
            } else if(sortDate.equals(DEFAULT) && !sortRating.equals(DEFAULT)) {
                reviews = reviewRepository.findAllByFacilityAndNotDeletedAndSortByRating(facility, sortRating);
            } else {
                reviews = reviewRepository.findAllByFacilityAndNotDeletedAndSortByRatingAndSortByDate(facility, sortRating, sortDate);
            }
        }
        return reviews.stream()
                .map(ReviewDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Review getModel(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Review with id %s not found.", id)));
    }

    @Override
    public ReviewDTO create(ReviewDTO reviewDTO, Long facilityId, String email) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityId)));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));

        if(!facility.getActive()) {
            throw new BadRequestException("Facility isn't active.");
        }

        reviewDTO.setFacility(facility);
        reviewDTO.setUser(user);
        reviewDTO.setCreatedAt(LocalDateTime.now());

        // count average
        facility.setTotalRating(calculateAverageRatingForFacility(reviewDTO, CREATE));
        facilityRepository.save(facility);

        rateService.create(RateDTO.convertToDto(reviewDTO.getRate()));
        return convertToDto(reviewRepository.save(reviewDTO.convertToModel()));
    }

    @Override
    public ReviewDTO update(ReviewDTO reviewDTO) {
        Review review = getModel(reviewDTO.getId());
        return convertToDto(reviewRepository.save(review));
    }

    @Override
    public void hide(Long reviewId) {
        Review review = getModel(reviewId);
        reviewRepository.hide(review.getId());
    }

    @Override
    public void delete(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review with id %s not found." + reviewId));
        Facility facility = facilityRepository.findById(review.getFacility().getId())
                .orElseThrow(() -> new NotFoundException("Facility not found with id: " + review.getFacility().getId()));
        review.setIsDeleted(true);
        facility.setTotalRating(calculateAverageRatingForFacility(convertToDto(review), DELETE));
        reviewRepository.deleteById(reviewId);
        facilityRepository.save(facility);
    }

    @Override
    public double calculateAverageRatingForFacility(ReviewDTO reviewDTO, String status) {
        List<Review> allReviewsForSpecificFacility = reviewRepository.findAllByFacilityAndNotDeleted(reviewDTO.getFacility());
        double curr = 0;
        int divideBy = 0;
        if(!allReviewsForSpecificFacility.isEmpty()) {
            for(Review review : allReviewsForSpecificFacility) {
                curr += review.getRate().getEquipment() + review.getRate().getHygene() + review.getRate().getSpace() + review.getRate().getStaff();
                divideBy += 4;
            }
        }
        if(status.equals(CREATE)) {
            curr += (reviewDTO.getRate().getEquipment() + reviewDTO.getRate().getStaff() + reviewDTO.getRate().getHygene() + reviewDTO.getRate().getSpace());
            divideBy += 4;
        }

        return curr/divideBy;
    }
}
