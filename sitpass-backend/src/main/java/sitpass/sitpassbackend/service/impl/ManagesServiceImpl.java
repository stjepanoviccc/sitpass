package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.ManagesDTO;
import sitpass.sitpassbackend.dto.ManagesRequestDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Facility;
import sitpass.sitpassbackend.model.Manages;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.repository.FacilityRepository;
import sitpass.sitpassbackend.repository.ManagesRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.ManagesService;

import java.util.List;
import java.util.stream.Collectors;

import static sitpass.sitpassbackend.dto.ManagesDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class ManagesServiceImpl implements ManagesService {

    public static final String facilityString = "facility";
    public static final String reviewString = "review";
    public static final String commentString = "comment";

    private final ManagesRepository managesRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Override
    public Manages getModel(Long id) {
        return managesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Manages with id %s not found.", id)));
    }

    @Override
    public Boolean isManager(String email, Long objectId, String by) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));
        return switch (by) {
            case facilityString -> managesRepository.isUserManagerForFacility(user.getId(), objectId);
            case reviewString -> managesRepository.isUserManagerForFacility(user.getId(), objectId);
            case commentString -> managesRepository.isUserManagerForFacility(user.getId(), objectId);
            default -> false;
        };
    }

    @Override
    public List<ManagesDTO> findAll() {
        List<Manages> manages = managesRepository.findAllByIsDeletedFalseOrIsDeletedNull();
        return manages.stream()
                .map(ManagesDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ManagesDTO> findAllByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));
        List<Manages> manages = managesRepository.findAllByUserAndNotDeleted(user);
        return manages.stream()
                .map(ManagesDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ManagesDTO create(ManagesRequestDTO managesRequestDTO) {
        Facility facility = facilityRepository.findByName(managesRequestDTO.getName())
                .orElseThrow(() -> new NotFoundException(String.format("Facility with name %s not found.", managesRequestDTO.getName())));
        User user = userRepository.findByEmail(managesRequestDTO.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", managesRequestDTO.getEmail())));

        ManagesDTO managesDTO;
        Manages manages = managesRepository.findByUserAndFacility(user, facility);

        if (manages != null && manages.getIsDeleted()) {
            throw new BadRequestException("Manages already exists in database.");
        } else {
            managesDTO = new ManagesDTO();
        }
        managesDTO.setUser(user);
        managesDTO.setFacility(facility);
        managesDTO.setStartDate(managesRequestDTO.getStartDate());
        managesDTO.setEndDate(managesRequestDTO.getEndDate());

        facility.setActive(true);
        facilityRepository.save(facility);

        return convertToDto(managesRepository.save(managesDTO.convertToModel()));
    }


    @Override
    public void delete(Long id) {
        ManagesDTO managesDTO = convertToDto(getModel(id));
        Facility facility = managesDTO.getFacility();

        List<Manages> allManages = managesRepository.findAllByFacilityAndNotDeleted(facility);
        if (allManages.size() <= 1) {
            facilityRepository.makeInactive(facility.getId());
        }

        managesRepository.deleteById(id);
    }
}