package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(DISTINCT e.user.id) " +
            "FROM User u " +
            "JOIN Facility f ON f.id = :facilityId " +
            "JOIN Exercise e ON e.facility.id = :facilityId " +
            "WHERE DATE_TRUNC(:level, e.from) = DATE_TRUNC(:level, CURRENT_DATE) ")
    Integer findUsersCount(@Param("facilityId") Long facilityId, @Param("level") String level);

    @Query("SELECT COUNT(DISTINCT e.user.id) " +
            "FROM Exercise e " +
            "JOIN Facility f ON f.id = :facilityId " +
            "WHERE e.facility.id = :facilityId AND e.from BETWEEN :dateFrom AND :dateTo AND e.until BETWEEN :dateFrom AND :dateTo")
    Integer findUsersCountByCustomRange(@Param("facilityId") Long facilityId, @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);

    @Query(value = "WITH ActivityHours AS ( " +
            "SELECT EXTRACT(HOUR FROM generate_series(e.from, e.until, '1 hour')) AS hour " +
            "FROM Exercise e " +
            "WHERE e.facility_id = :facilityId AND DATE_TRUNC(:period, e.from) = DATE_TRUNC(:period, CURRENT_DATE) " +
            ") " +
            "SELECT hour AS PEAK_HOUR, COALESCE(COUNT(*), 0) AS EXERCISES_FOR_SPECIFIC_HOURS_COUNT " +
            "FROM ActivityHours " +
            "GROUP BY hour " +
            "ORDER BY CASE WHEN :sort = 'asc' THEN COALESCE(COUNT(*), 0) END ASC, " +
            "         CASE WHEN :sort = 'desc' THEN COALESCE(COUNT(*), 0) END DESC " +
            "LIMIT 1", nativeQuery = true)
    Map<Integer, Integer> findPeakHours(@Param("facilityId") Long facilityId, @Param("period") String period, @Param("sort") String sort);

    @Query(value = "WITH ActivityHours AS ( " +
            "SELECT EXTRACT(HOUR FROM generate_series(e.from, e.until, '1 hour')) AS hour " +
            "FROM Exercise e " +
            "WHERE e.facility_id = :facilityId AND e.from BETWEEN :dateFrom AND :dateTo AND e.until BETWEEN :dateFrom AND :dateTo " +
            ") " +
            "SELECT hour AS PEAK_HOUR, COALESCE(COUNT(*), 0) AS EXERCISES_FOR_SPECIFIC_HOURS_COUNT " +
            "FROM ActivityHours " +
            "GROUP BY hour " +
            "ORDER BY CASE WHEN :sort = 'asc' THEN COALESCE(COUNT(*), 0) END ASC, " +
            "         CASE WHEN :sort = 'desc' THEN COALESCE(COUNT(*), 0) END DESC " +
            "LIMIT 1", nativeQuery = true)
    Map<Integer, Integer> findPeakHoursByCustomRange(@Param("facilityId") Long facilityId, @Param("sort") String sort,
                                                     @Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);


}
