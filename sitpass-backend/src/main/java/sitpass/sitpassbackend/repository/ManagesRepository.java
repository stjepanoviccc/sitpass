package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Manages;
import sitpass.sitpassbackend.model.User;

import java.util.List;

public interface ManagesRepository extends JpaRepository<Manages, Long> {
    Manages findByUserAndFacility(User user, Facility facility);

    @Query("SELECT m FROM Manages m WHERE m.user = :user AND (m.isDeleted = false OR m.isDeleted IS NULL)")
    List<Manages> findAllByUserAndNotDeleted(@Param("user") User user);

    @Query("SELECT m FROM Manages m WHERE m.facility = :facility AND (m.isDeleted = false OR m.isDeleted IS NULL)")
    List<Manages> findAllByFacilityAndNotDeleted(@Param("facility") Facility facility);

    List<Manages> findAllByIsDeletedFalseOrIsDeletedNull();

    @Transactional
    @Modifying
    @Query("UPDATE Manages m SET m.isDeleted = true WHERE m.id = :id")
    void deleteById(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN TRUE ELSE FALSE END FROM Manages m WHERE m.user.id = :userId AND m.facility.id = :facilityId")
    Boolean isUserManagerForFacility(@Param("userId") Long userId, @Param("facilityId") Long facilityId);
}
