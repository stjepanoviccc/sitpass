package sitpass.sitpassbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.DisciplineDTO;
import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.service.DisciplineService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities/{facilityId}/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    @PutMapping()
    public ResponseEntity<DisciplineDTO> updateDiscipline(@PathVariable FacilityDTO facility, @RequestBody DisciplineDTO disciplineDTO) {
        return ResponseEntity.ok(disciplineService.update(facility.convertToModel(), disciplineDTO));
    }

    @DeleteMapping("/{disciplineId}")
    public ResponseEntity<Void> deleteDiscipline(@PathVariable Long disciplineId) {
        disciplineService.delete(disciplineId);
        return ResponseEntity.noContent().build();
    }
}
