package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.*;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.*;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.specs.FilterFacilitySpecification;
import sitpass.sitpassbackend.service.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static sitpass.sitpassbackend.dto.FacilityDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final WorkDayService workDayService;
    private final DisciplineService disciplineService;
    private final ImageService imageService;
    private final UserService userService;

    @Override
    public List<FacilityDTO> findAll() {
        List<Facility> facilities = facilityRepository.findAll();
        return facilities.stream()
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDTO> findAllByActiveTrueAndCity(String email, PaginationDTO paginationDTO) {
        User user = userService.getModel(email);
        List<Facility> facilities;
        Pageable pageable = PageRequest.of(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());

        if(user.getCity() != null && !user.getCity().isEmpty()) {
            Page<Facility> facilityPage = facilityRepository.findAllByActiveTrueAndCity(user.getCity(), pageable);
            facilities = facilityPage.getContent();
        } else {
            facilities = facilityRepository.findAllByActiveTrue(pageable);
        }

        return facilities.stream()
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDTO> findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(String email, PaginationDTO paginationDTO) {
        User user = userService.getModel(email);
        List<Facility> facilities;
        Pageable pageable = PageRequest.of(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());

        if(user.getCity() != null && !user.getCity().isEmpty()) {
            Page<Facility> facilityPage = facilityRepository.findAllByActiveTrueAndCityAndTotalRatingIsNotNullOrderByTotalRatingDesc(user.getCity(), pageable);
            facilities = facilityPage.getContent();
        } else {
            facilities = facilityRepository.findAllByActiveTrueAndTotalRatingIsNotNullOrderByTotalRatingDesc(pageable);
        }

        return facilities.stream()
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDTO> findAllByFrequentlyVisited(String email, PaginationDTO paginationDTO) {
        User user = userService.getModel(email);
        Pageable pageable = PageRequest.of(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());
        Page<Facility> facilityPage = facilityRepository.findAllByFrequentlyVisited(user.getId(), pageable);
        List<Facility> facilities = facilityPage.getContent();
        return facilities.stream()
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDTO> findAllByExploreNew(String email, PaginationDTO paginationDTO) {
        User user = userService.getModel(email);
        Pageable pageable = PageRequest.of(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());
        Page<Facility> facilityPage = facilityRepository.findAllByExploreNew(user.getId(), pageable);
        List<Facility> facilities = facilityPage.getContent();
        return facilities.stream()
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityDTO> filterAll(FilterFacilityDTO filterFacilityDTO, PaginationDTO paginationDTO) {
        Pageable pageable = PageRequest.of(paginationDTO.getCurrentPage(), paginationDTO.getPageSize());
        List<String> cities = new ArrayList<>();
        List<String> disciplines = new ArrayList<>();

        if (filterFacilityDTO.getCities() != null && !filterFacilityDTO.getCities().isEmpty()) {
            cities = Arrays.stream(filterFacilityDTO.getCities().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }
        if (filterFacilityDTO.getDisciplines() != null && !filterFacilityDTO.getDisciplines().isEmpty()) {
            disciplines = Arrays.stream(filterFacilityDTO.getDisciplines().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        Specification<Facility> spec = FilterFacilitySpecification.withOptionalFields(
                cities, disciplines, filterFacilityDTO.getMinTotalRating(), filterFacilityDTO.getMaxTotalRating(),
                filterFacilityDTO.getWorkDay(), filterFacilityDTO.getFrom(), filterFacilityDTO.getUntil()
        );

        // Page<Facility> facilityPage = facilityRepository.findAll(spec, pageable);
        // List<Facility> facilities = facilityPage.getContent();
        int skip = paginationDTO.getCurrentPage() * paginationDTO.getPageSize();
        int pageSize = paginationDTO.getPageSize();

        List<Facility> facilities = facilityRepository.findAll(spec);

        return facilities.stream()
                .skip(skip)
                .limit(pageSize)
                .map(FacilityDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Facility getModel(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", id)));
    }

    @Override
    public FacilityDTO create(FacilityDTO facilityDTO, String email) {
        Optional<Facility> existingFacility = facilityRepository.findByName(facilityDTO.getName());
        if(existingFacility.isPresent()) {
            throw new BadRequestException("Facility with this name already exist.");
        }

        facilityDTO.setCreatedAt(LocalDate.now());
        Facility facility = facilityRepository.save(facilityDTO.convertToModel());
        facility.setActive(false);

        for (WorkDay workDay : facility.getWorkDays()) {
            workDay.setFacility(facility);
            workDayService.create(WorkDayDTO.convertToDto(workDay));
        }

        for (Discipline discipline : facility.getDisciplines()) {
            discipline.setFacility(facility);
            disciplineService.create(DisciplineDTO.convertToDto(discipline));
        }

        for (Image image : facility.getImages()) {
            image.setFacility(facility);
            imageService.handleImageCreation(image, convertToDto(facility), null);
        }

        return convertToDto(facility);
    }

    @Override
    public FacilityDTO update(FacilityDTO facilityDTO) {
        Facility existingFacility = facilityRepository.findById(facilityDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityDTO.getId())));

        existingFacility.getWorkDays().clear();
        existingFacility.getDisciplines().clear();
        existingFacility.setName(facilityDTO.getName());
        existingFacility.setAddress(facilityDTO.getAddress());
        existingFacility.setCity(facilityDTO.getCity());
        existingFacility.setDescription(facilityDTO.getDescription());

        existingFacility = updateDisciplinesAndWorkDaysAndImages(facilityDTO);

        Facility updatedFacility = facilityRepository.save(existingFacility);
        return convertToDto(updatedFacility);
    }

    @Override
    public FacilityDTO updateByManager(FacilityDTO facilityDTO) {
        Facility existingFacility = facilityRepository.findById(facilityDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityDTO.getId())));

        existingFacility.getWorkDays().clear();
        existingFacility.getDisciplines().clear();

        existingFacility = updateDisciplinesAndWorkDaysAndImages(facilityDTO);

        Facility updatedFacility = facilityRepository.save(existingFacility);
        return convertToDto(updatedFacility);
    }

    @Override
    public void delete(Long id) {
        Facility facility = getModel(id);

        for (WorkDay workDay : facility.getWorkDays()) { workDayService.delete(workDay.getId()); }
        for (Discipline discipline : facility.getDisciplines()) { disciplineService.delete(discipline.getId());  }
        for (Image image : facility.getImages()) {  imageService.delete(image.getId()); }

        facilityRepository.deleteById(id);
    }

    @Override
    public Facility updateDisciplinesAndWorkDaysAndImages(FacilityDTO facilityDTO) {
        Facility existingFacility = facilityRepository.findById(facilityDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Facility with id %s not found.", facilityDTO.getId())));

        for (WorkDay workDay : facilityDTO.getWorkDays()) {
            workDay.setFacility(existingFacility);
            existingFacility.getWorkDays().add(workDay);
        }

        for (Discipline discipline : facilityDTO.getDisciplines()) {
            discipline.setFacility(existingFacility);
            existingFacility.getDisciplines().add(discipline);
        }

        // logical deleting all
        for (Image existingImage : existingFacility.getImages()) {
            existingImage.setIsDeleted(true);
            imageService.delete(existingImage.getId());
        }

        // re-adding each and also adding new
        List<Image> newImageList = new ArrayList<>();
        for (Image image : facilityDTO.getImages()) {
            if(image.getId() != null) {
                image.setFacility(facilityDTO.convertToModel());
                newImageList.add(image);
            } else {
                if(image.getPath().length() > 4) {
                    imageService.handleImageCreation(image, facilityDTO, null);
                    newImageList.add(image);
                }
            }
        }
        existingFacility.setImages(newImageList);
        return existingFacility;
    }


}
