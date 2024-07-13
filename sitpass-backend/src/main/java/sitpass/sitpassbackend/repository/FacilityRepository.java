package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.Facility;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {

    @Modifying
    @Query("SELECT f FROM Facility f WHERE f.isDeleted = false OR f.isDeleted IS NULL")
    List<Facility> findAll();
    List<Facility> findAll(Specification<Facility> spec);
    Page<Facility> findAll(Specification<Facility> spec, Pageable pageable);

    Optional<Facility> findByName(@NotBlank String name);

    List<Facility> findAllByActiveTrue(Pageable pageable);
    Page<Facility> findAllByActiveTrueAndCity(String city, Pageable pageable);

    List<Facility> findAllByActiveTrueAndTotalRatingIsNotNullOrderByTotalRatingDesc(Pageable pageable);
    Page<Facility> findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(String city, Pageable pageable);

    @Query("SELECT f FROM Facility f " +
            "JOIN Exercise e ON f.id = e.facility.id " +
            "JOIN User u ON e.user.id = :userId " +
            "WHERE f.active = true " +
            "GROUP BY f.id " +
            "ORDER BY MAX(e.until) DESC")
    Page<Facility> findAllByFrequentlyVisited(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT f " +
            "FROM Facility f " +
            "JOIN Exercise e ON f.id = e.facility.id " +
            "JOIN User u ON e.user.id != :userId " +
            "JOIN Discipline d ON d.facility.id = f.id " +
            "WHERE f.active = true AND d.name NOT IN ( " +
            "    SELECT d2.name " +
            "    FROM Facility f2 " +
            "    JOIN Exercise e2 ON f2.id = e2.facility.id " +
            "    JOIN User u2 ON e2.user.id = :userId " +
            "    JOIN Discipline d2 ON f2.id = d2.facility.id " +
            ") " +
            "GROUP BY f.id ")
    Page<Facility> findAllByExploreNew(@Param("userId") Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Facility f SET f.isDeleted = true WHERE f.id = :id")
    void deleteById(@Param("id") Long id);

    // making active i handle in service impl. this one is called after manager deletion so i had to bring different query.
    @Transactional
    @Modifying
    @Query("UPDATE Facility f SET f.active = false WHERE f.id = :id")
    void makeInactive(@Param("id") Long id);
}
