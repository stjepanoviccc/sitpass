package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.dto.FilterFacilityDTO;
import sitpass.sitpassbackend.dto.PaginationDTO;
import sitpass.sitpassbackend.model.Facility;

import java.util.List;

public interface FacilityService {
    List<FacilityDTO> findAll();
    List<FacilityDTO> findAllByActiveTrueAndCity(String email, PaginationDTO paginationDTO);
    List<FacilityDTO> findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(String email, PaginationDTO paginationDTO);
    List<FacilityDTO> findAllByFrequentlyVisited(String email, PaginationDTO paginationDTO);
    List<FacilityDTO> findAllByExploreNew(String email, PaginationDTO paginationDTO);
    List<FacilityDTO> filterAll(FilterFacilityDTO filterFacilityDTO, PaginationDTO paginationDTO);
    Facility getModel(Long id);
    FacilityDTO create(FacilityDTO facilityDTO, String email);
    FacilityDTO update(FacilityDTO facilityDTO);
    FacilityDTO updateByManager(FacilityDTO facilityDTO);
    void delete(Long id);

    // helper
    Facility updateDisciplinesAndWorkDaysAndImages(FacilityDTO facilityDTO);
}
