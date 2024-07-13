package sitpass.sitpassbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sitpass.sitpassbackend.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}
