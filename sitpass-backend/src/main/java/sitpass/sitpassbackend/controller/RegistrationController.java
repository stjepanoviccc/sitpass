package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.AccountRequestDTO;
import sitpass.sitpassbackend.service.RegistrationService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static sitpass.sitpassbackend.config.SecurityConfiguration.ROLE_ADMIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/registrations/accountRequests")
public class RegistrationController {

    private final RegistrationService registrationService;

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @GetMapping()
    public ResponseEntity<List<AccountRequestDTO>> findAllPendingAccountRequests() {
        return ResponseEntity.ok(registrationService.findAllPending());
    }

    @PostMapping()
    public ResponseEntity<AccountRequestDTO> sendAccountRequest(@Valid @RequestBody AccountRequestDTO accountRequestDTO) {
        return ResponseEntity.status(CREATED).body(registrationService.sendAccountRequest(accountRequestDTO));
    }

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @PutMapping()
    public ResponseEntity<AccountRequestDTO> handleAccountRequest(@RequestBody AccountRequestDTO accountRequestDTO) {
        return ResponseEntity.ok(registrationService.handleAccountRequest(accountRequestDTO));
    }

}