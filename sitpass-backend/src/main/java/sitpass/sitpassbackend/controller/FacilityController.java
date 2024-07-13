package sitpass.sitpassbackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.dto.FilterFacilityDTO;
import sitpass.sitpassbackend.dto.PaginationDTO;
import sitpass.sitpassbackend.service.FacilityService;
import sitpass.sitpassbackend.service.ManagesService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static sitpass.sitpassbackend.config.SecurityConfiguration.ROLE_ADMIN;
import static sitpass.sitpassbackend.dto.FacilityDTO.convertToDto;
import static sitpass.sitpassbackend.service.impl.ManagesServiceImpl.facilityString;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityService facilityService;
    private final ManagesService managesService;

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @GetMapping()
    public ResponseEntity<List<FacilityDTO>> findAll() {
        return ResponseEntity.ok(facilityService.findAll());
    }

    @GetMapping("/page/{currentPage}/size/{pageSize}")
    public ResponseEntity<List<FacilityDTO>> findAllByActiveTrueAndCity(@PathVariable int currentPage, @PathVariable int pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO(currentPage, pageSize);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(facilityService.findAllByActiveTrueAndCity(email, paginationDTO));
    }

    @GetMapping("/page/{currentPage}/size/{pageSize}/most_popular")
    public ResponseEntity<List<FacilityDTO>> findAllByActiveTrueAndCityAndTotalRatingNotNullOrderByTotalRatingDesc(@PathVariable int currentPage, @PathVariable int pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO(currentPage, pageSize);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(facilityService.findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(email, paginationDTO));
    }

    @GetMapping("/page/{currentPage}/size/{pageSize}/frequently_visited")
    public ResponseEntity<List<FacilityDTO>> findAllByFrequentlyVisited(@PathVariable int currentPage, @PathVariable int pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO(currentPage, pageSize);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(facilityService.findAllByFrequentlyVisited(email, paginationDTO));
    }

    @GetMapping("/page/{currentPage}/size/{pageSize}/explore_new")
    public ResponseEntity<List<FacilityDTO>> findAllByExploreNew(@PathVariable int currentPage, @PathVariable int pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO(currentPage, pageSize);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(facilityService.findAllByExploreNew(email, paginationDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(facilityService.getModel(id)));
    }

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @PostMapping()
    public ResponseEntity<FacilityDTO> createFacility(@Valid @RequestBody FacilityDTO facilityDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(CREATED).body(facilityService.create(facilityDTO, email));
    }

    @PostMapping("/page/{currentPage}/size/{pageSize}/filter")
    public ResponseEntity<List<FacilityDTO>> filterAll(@RequestBody FilterFacilityDTO filterFacilityDTO, @PathVariable int currentPage, @PathVariable int pageSize) {
        PaginationDTO paginationDTO = new PaginationDTO(currentPage, pageSize);
        return ResponseEntity.ok(facilityService.filterAll(filterFacilityDTO, paginationDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityDTO> updateFacility(@Valid @RequestBody FacilityDTO facilityDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(role.equals(ROLE_ADMIN)) {
            return ResponseEntity.ok(facilityService.update(facilityDTO));
        } else {
            Boolean isManager = managesService.isManager(email, facilityDTO.getId(), facilityString);
            if(isManager) {
                return ResponseEntity.ok(facilityService.updateByManager(facilityDTO));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }
    }

    @PreAuthorize("hasRole('" + ROLE_ADMIN + "')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFacility(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
