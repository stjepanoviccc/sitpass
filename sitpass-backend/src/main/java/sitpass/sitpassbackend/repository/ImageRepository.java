package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.Image;
import sitpass.sitpassbackend.model.User;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Image i SET i.isDeleted = true WHERE i.id = :id")
    void deleteById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Image i SET i.isDeleted = true WHERE i.user = :user")
    void deleteByUser(@Param("user") User user);

    Optional<Image> findByUser(User user);

    Optional<Image> findByIsDeletedFalseOrIsDeletedIsNullAndUser(User user);
}
