package sitpass.sitpassbackend.aspect;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.model.AuditLog;
import sitpass.sitpassbackend.repository.AuditLogRepository;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private static final Logger logger = LogManager.getLogger(AuditLoggingAspect.class);
    private final AuditLogRepository auditLogRepository;

    @AfterReturning(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.create(..)) && args(facilityDTO, email)",
            returning = "createdFacilityDTO")
    public void logFacilityCreationSuccess(FacilityDTO facilityDTO, String email, FacilityDTO createdFacilityDTO) {
        logAuditEntry("CREATE", facilityDTO.getId(), LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.create(..)) && args(facilityDTO, email)",
            throwing = "exception")
    public void logFacilityCreationFailure(FacilityDTO facilityDTO, String email, Exception exception) {
        logAuditEntry("CREATE", facilityDTO.getId(), LocalDateTime.now(), false, exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.update(..)) && args(facilityDTO)",
            returning = "updatedFacilityDTO", argNames = "facilityDTO,updatedFacilityDTO")
    public void logFacilityUpdateSuccess(FacilityDTO facilityDTO, FacilityDTO updatedFacilityDTO) {
        logAuditEntry("UPDATE", facilityDTO.getId(), LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.update(..)) && args(facilityDTO)",
            throwing = "exception", argNames = "facilityDTO,exception")
    public void logFacilityUpdateFailure(FacilityDTO facilityDTO, Exception exception) {
        logAuditEntry("UPDATE", facilityDTO.getId(), LocalDateTime.now(), false, exception.getMessage());
    }

    @AfterReturning(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.delete(..)) && args(id)")
    public void logFacilityDeletionSuccess(Long id) {
        logAuditEntry("DELETE", id, LocalDateTime.now(), true, null);
    }

    @AfterThrowing(pointcut = "execution(* sitpass.sitpassbackend.service.FacilityService.delete(..)) && args(id)", throwing = "exception")
    public void logFacilityDeletionFailure(Long id, Exception exception) {
        logAuditEntry("DELETE", id, LocalDateTime.now(), false, exception.getMessage());
    }

    private void logAuditEntry(String action, Long entityId, LocalDateTime timestamp, boolean success, String errorMessage) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        if (success) {
            logger.info(String.format("Action [%s] on Facility with id [%d] performed by User [%s] at [%s] was successful",
                    action, entityId, username, timestamp));
        } else {
            logger.error(String.format("Action [%s] on Facility with id [%d] performed by User [%s] at [%s] failed with error: %s",
                    action, entityId, username, timestamp, errorMessage));
        }

        AuditLog auditLog = new AuditLog();
        auditLog.setAction(action);
        auditLog.setEntity_id(entityId);
        auditLog.setUsername(username);
        auditLog.setCreated_at(timestamp);
        auditLog.setSuccess(success);
        auditLog.setErrorMessage(errorMessage);

        auditLogRepository.save(auditLog);
    }
}
