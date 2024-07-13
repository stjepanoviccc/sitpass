package sitpass.sitpassbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sitpass.sitpassbackend.model.Discipline;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Discipline d SET d.isDeleted = true WHERE d.id = :id")
    void deleteById(@Param("id") Long id);
}
