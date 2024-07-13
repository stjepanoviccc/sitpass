package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.ManagesDTO;
import sitpass.sitpassbackend.dto.ManagesRequestDTO;
import sitpass.sitpassbackend.service.ManagesService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static sitpass.sitpassbackend.config.SecurityConfiguration.ROLE_ADMIN;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/manages")
public class ManagesController {

    private final ManagesService managesService;

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @GetMapping()
    public ResponseEntity<List<ManagesDTO>> findAll() {
        return ResponseEntity.ok(managesService.findAll());
    }

    @GetMapping("/users")
    public ResponseEntity<List<ManagesDTO>> findAllByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(managesService.findAllByUser(email));
    }

    @GetMapping("/facilities/{objectId}/{by}")
    public ResponseEntity<Boolean> isManager(@PathVariable Long objectId, @PathVariable String by) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(managesService.isManager(email, objectId, by));
    }

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @PostMapping()
    public ResponseEntity<ManagesDTO> createManages(@Valid @RequestBody ManagesRequestDTO managesRequestDTO) {
        return ResponseEntity.status(CREATED).body(managesService.create(managesRequestDTO));
    }

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        managesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
