package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sitpass.sitpassbackend.model.AccountRequest;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<AccountRequest, Long> {

    @Query("SELECT ar FROM AccountRequest ar WHERE ar.requestStatus = 'PENDING'")
    List<AccountRequest> findAllPending();

    Optional<AccountRequest> findByEmail(String email);

}
