package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sitpass.sitpassbackend.model.Exercise;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.User;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findAllByFacilityAndUser(Facility facility, User user);

    List<Exercise> findAllByUser(User user);
}
