package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sitpass.sitpassbackend.dto.ReviewDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.*;
import sitpass.sitpassbackend.model.enums.DayOfWeek;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.ReviewRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.impl.FacilityServiceImpl;
import sitpass.sitpassbackend.service.impl.ReviewServiceImpl;
import sitpass.sitpassbackend.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.ReviewDTO.convertToDto;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    private final Review review = createReview(1L);
    private final List<Review> reviews = Arrays.asList(createReview(10L), createReview(20L));
    private final User user = createUser();
    private final Facility facility = createFacility();
    private final Rate rate = new Rate(1L, 5, 5, 5, 5, false);
    private final Comment comment = new Comment();

    private Review createReview(Long id) {
        return Review.builder()
                .id(id)
                .createdAt(LocalDateTime.now())
                .rate(rate)
                .comment(comment)
                .exerciseCount(1)
                .facility(facility)
                .user(user)
                .hidden(false)
                .isDeleted(false)
                .build();
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("email@gmail.com")
                .build();
    }

    private Facility createFacility() {
        return Facility.builder()
                .id(1L)
                .workDays(Collections.singletonList(createWorkDay()))
                .disciplines(new ArrayList<Discipline>())
                .active(true)
                .build();
    }

    private WorkDay createWorkDay() {
        return WorkDay.builder()
                .id(1L)
                .validFrom(LocalDate.MIN)
                .from(LocalTime.MIN)
                .until(LocalTime.MAX)
                .facility(facility)
                .isDeleted(false)
                .day(DayOfWeek.MONDAY)
                .build();
    }

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private RateService rateService;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void shouldFindAllByUser_whenFindAllByUser_ifUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(reviewRepository.findAllByUserAndNotDeleted(user)).thenReturn(reviews);

        List<ReviewDTO> reviewDTOList = reviewService.findAllByUser(user.getEmail());

        assertNotNull(reviewDTOList);

        verify(userRepository).findByEmail(user.getEmail());
        verify(reviewRepository).findAllByUserAndNotDeleted(user);
        verifyNoMoreInteractions(userRepository, reviewRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindAllByUser_ifUserDoesNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        userNotFoundByEmailAssertion();

        verify(userRepository).findByEmail(user.getEmail());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldFindAllByFacility_whenFindAllByFacility_ifFacilityExists() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(reviewRepository.findAllByFacilityAndNotDeleted(facility)).thenReturn(reviews);

        List<ReviewDTO> reviewDTOList = reviewService.findAllByFacility(facility.getId());

        assertNotNull(reviewDTOList);

        verify(facilityRepository).findById(facility.getId());
        verify(reviewRepository).findAllByFacilityAndNotDeleted(facility);
        verifyNoMoreInteractions(facilityRepository, reviewRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenFindAllByFacility_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        facilityNotFoundByIdAssertion();

        verify(facilityRepository).findById(facility.getId());
        verifyNoMoreInteractions(facilityRepository);
    }

    @Test
    void shouldGetReview_whenGetModel_ifReviewExists() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        ReviewDTO reviewDTO = convertToDto(reviewService.getModel(review.getId()));

        assertNotNull(reviewDTO);
        assertEquals(review.getId(), reviewDTO.getId());
        assertEquals(review.getUser(), reviewDTO.getUser());
        assertEquals(review.getFacility(), reviewDTO.getFacility());
        assertEquals(review.getRate(), reviewDTO.getRate());

        verify(reviewRepository).findById(review.getId());
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    void shouldThrowNotFoundException_whenGetModel_ifReviewDoesNotExist() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        reviewNotFoundByIdAssertion();

        verify(reviewRepository).findById(review.getId());
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    void shouldCreateReview_whenCreate_ifFacilityAndUserExistsAndFacilityIsActive() {
        review.setFacility(facility);
        review.setUser(user);
        review.setRate(rate);
        facility.setActive(true);

        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO createdReviewDTO = reviewService.create(convertToDto(review), facility.getId(), user.getEmail());

        assertNotNull(createdReviewDTO);
        assertEquals(createdReviewDTO.getId(), review.getId());
        assertEquals(createdReviewDTO.getFacility(), review.getFacility());
        assertEquals(createdReviewDTO.getUser(), review.getUser());

        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void shouldThrowNotFoundException_whenCreate_ifFacilityDoesNotExist() {
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.empty());

        facilityNotFoundByIdAssertion();

        verify(facilityRepository).findById(facility.getId());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldThrowNotFoundException_whenCreate_ifUserDoesNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        userNotFoundByEmailAssertion();

        verify(userRepository).findByEmail(user.getEmail());
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifReviewExistsAndFacilityIsNotActive() {
        review.setFacility(facility);
        review.setUser(user);
        review.getFacility().setActive(false);

        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class, () ->
                reviewService.create(convertToDto(review), review.getFacility().getId(), review.getUser().getEmail()));
        assertEquals("Facility isn't active.", exception.getMessage());

        verify(reviewRepository, never()).save(any());
    }


    @Test
    void shouldUpdateReview_whenUpdate_ifReviewExists() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO updatedReviewDTO = reviewService.update(convertToDto(review));

        assertNotNull(updatedReviewDTO);
        assertEquals(review.getId(), updatedReviewDTO.getId());
        assertEquals(review.getCreatedAt(), updatedReviewDTO.getCreatedAt());

        verify(reviewRepository).findById(review.getId());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifReviewDoesNotExist() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        reviewNotFoundByIdAssertion();

        verify(reviewRepository).findById(review.getId());
        verify(reviewRepository, never()).save(any(Review.class));
    }


    @Test
    void shouldHideReview_whenHide_ifReviewExists() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        reviewService.hide(review.getId());

        verify(reviewRepository).findById(review.getId());
        verify(reviewRepository).hide(review.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenHide_ifReviewDoesNotExist() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        reviewNotFoundByIdAssertion();

        verify(reviewRepository).findById(review.getId());
        verify(reviewRepository, never()).hide(review.getId());
    }

    @Test
    void shouldDelete_whenDelete_ifReviewExists() {
        review.setFacility(facility);
        review.getFacility().setId(1L);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(facilityRepository.findById(facility.getId())).thenReturn(Optional.of(facility));

        reviewService.delete(review.getId());

        verify(reviewRepository).findById(review.getId());
        verify(facilityRepository).findById(facility.getId());
        verify(reviewRepository).deleteById(review.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenDelete_ifReviewDoesNotExist() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());

        reviewNotFoundByIdAssertion();

        verify(reviewRepository).findById(review.getId());
        verify(reviewRepository, never()).delete(any(Review.class));
    }

    // helping
    void reviewNotFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> reviewService.getModel(review.getId()));
        assertEquals(String.format("Review with id %s not found.", review.getId()), exception.getMessage());
    }

    void userNotFoundByEmailAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getModel(user.getEmail()));
        assertEquals(String.format("User with email %s not found.", user.getEmail()), exception.getMessage());
    }

    private void facilityNotFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> facilityService.getModel(facility.getId()));
        assertEquals(String.format("Facility with id %s not found.", facility.getId()), exception.getMessage());
    };

}
