package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.WorkDayDTO;
import sitpass.sitpassbackend.service.WorkDayService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities/{facilityId}/workDays")
public class WorkDayController {

    private final WorkDayService workDayService;

    @PutMapping("/{id}")
    public ResponseEntity<WorkDayDTO> updateWorkDay(@Valid @PathVariable Long id, @RequestBody WorkDayDTO workDayDTO) {
        return ResponseEntity.ok(workDayService.update(id, workDayDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkDay(@PathVariable Long id) {
        workDayService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
