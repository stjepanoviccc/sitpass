package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.ReviewDTO;
import sitpass.sitpassbackend.model.Review;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> findAllByUser(String email);
    List<ReviewDTO> findAllByFacility(Long facilityId);
    List<ReviewDTO> findAllByFacilityAndSort(Long facilityId, String sortRating, String sortDate);
    Review getModel(Long id);
    ReviewDTO create(ReviewDTO reviewDTO, Long facilityId, String email);
    ReviewDTO update(ReviewDTO reviewDTO);
    void hide(Long id);
    void delete(Long reviewId);
    double calculateAverageRatingForFacility(ReviewDTO reviewDTO, String status);
}
