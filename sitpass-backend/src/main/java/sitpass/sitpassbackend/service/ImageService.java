package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.dto.ImageDTO;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.model.Image;

import java.util.Optional;

public interface ImageService {

    Image getModel(Long id);
    Optional<Image> findActiveByUser(String email);
    ImageDTO create(ImageDTO imageDTO);
    ImageDTO update(Long id, ImageDTO imageDTO);
    void delete(Long id);

    // helping
    void handleImageCreation(Image image, FacilityDTO facilityDTO, UserDTO userDTO);

}
