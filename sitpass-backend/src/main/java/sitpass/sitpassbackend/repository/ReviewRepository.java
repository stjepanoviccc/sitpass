package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Review;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.user = :user AND (r.isDeleted = false OR r.isDeleted IS NULL)")
    List<Review> findAllByUserAndNotDeleted(@Param("user") User user);

    @Query("SELECT r FROM Review r WHERE r.facility = :facility AND (r.isDeleted = false OR r.isDeleted IS NULL)")
    List<Review> findAllByFacilityAndNotDeleted(@Param("facility") Facility facility);

    @Query("SELECT r FROM Review r JOIN r.rate rate " +
            "WHERE r.facility = :facility AND (r.isDeleted = false OR r.isDeleted IS NULL) " +
            "ORDER BY " +
            "CASE WHEN :sortRating = 'ASC' THEN (rate.equipment + rate.hygene + rate.staff + rate.space) END ASC NULLS FIRST, " +
            "CASE WHEN :sortRating = 'DESC' THEN (rate.equipment + rate.hygene + rate.staff + rate.space) END DESC NULLS LAST, " +
            "CASE WHEN :sortDate = 'ASC' THEN r.createdAt END ASC NULLS FIRST, " +
            "CASE WHEN :sortDate = 'DESC' THEN r.createdAt END DESC NULLS LAST " )
    List<Review> findAllByFacilityAndNotDeletedAndSortByRatingAndSortByDate(@Param("facility") Facility facility, @Param("sortRating") String sortRating, @Param("sortDate") String sortDate);

    @Query("SELECT r FROM Review r JOIN r.rate rate " +
            "WHERE r.facility = :facility AND (r.isDeleted = false OR r.isDeleted IS NULL) " +
            "ORDER BY " +
            "CASE WHEN :sortRating = 'ASC' THEN (rate.equipment + rate.hygene + rate.staff + rate.space) END ASC NULLS FIRST, " +
            "CASE WHEN :sortRating = 'DESC' THEN (rate.equipment + rate.hygene + rate.staff + rate.space) END DESC NULLS LAST ")
    List<Review> findAllByFacilityAndNotDeletedAndSortByRating(@Param("facility") Facility facility, @Param("sortRating") String sortRating);

    @Query("SELECT r FROM Review r JOIN r.rate rate " +
            "WHERE r.facility = :facility AND (r.isDeleted = false OR r.isDeleted IS NULL) " +
            "ORDER BY " +
            "CASE WHEN :sortDate = 'ASC' THEN r.createdAt END ASC NULLS FIRST, " +
            "CASE WHEN :sortDate = 'DESC' THEN r.createdAt END DESC NULLS LAST " )
    List<Review> findAllByFacilityAndNotDeletedAndSortByDate(@Param("facility") Facility facility, @Param("sortDate") String sortDate);

    @Query("SELECT COUNT(DISTINCT r) " +
            "FROM Review r " +
            "JOIN Facility f ON f.id = :facilityId " +
            "WHERE r.facility.id = :facilityId AND DATE_TRUNC(:level, r.createdAt) = DATE_TRUNC(:level, CURRENT_DATE) ")
    Integer findReviewsCount(@Param("facilityId") Long facilityId, @Param("level") String level);

    @Query("SELECT COUNT(DISTINCT r) " +
            "FROM Review r " +
            "JOIN Facility f ON f.id = :facilityId " +
            "WHERE r.facility.id = :facilityId AND r.createdAt BETWEEN :dateFrom AND :dateTo")
    Integer findReviewsCountByCustomRange(@Param("facilityId") Long facilityId, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.hidden = true WHERE r.id = :id")
    void hide(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.isDeleted = true WHERE r.id = :id")
    void deleteById(@Param("id") Long id);

}
