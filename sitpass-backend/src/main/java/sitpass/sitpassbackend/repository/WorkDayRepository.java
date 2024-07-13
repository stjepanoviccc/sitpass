package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.WorkDay;

public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE WorkDay wd SET wd.isDeleted = true WHERE wd.id = :id")
    void deleteById(@Param("id") Long id);
}
