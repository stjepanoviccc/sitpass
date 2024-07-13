package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sitpass.sitpassbackend.model.Rate;

public interface RateRepository extends JpaRepository<Rate, Long> {
}
