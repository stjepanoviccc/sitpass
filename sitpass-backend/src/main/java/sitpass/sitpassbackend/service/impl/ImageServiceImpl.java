package sitpass.sitpassbackend.service.impl;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.FacilityDTO;
import sitpass.sitpassbackend.dto.ImageDTO;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.Image;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.repository.ImageRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.FileService;
import sitpass.sitpassbackend.service.ImageService;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static sitpass.sitpassbackend.config.AppConfig.IMAGE_PATH;
import static sitpass.sitpassbackend.dto.ImageDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Override
    public Image getModel(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Image with id %s not found.", id)));
    }

    public Optional<Image> findActiveByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    String errorMessage = String.format("User with email %s not found.", email);
                    return new NotFoundException(errorMessage);
                });
        return imageRepository.findByIsDeletedFalseOrIsDeletedIsNullAndUser(user);
    }

    @Override
    public ImageDTO create(ImageDTO imageDTO) {
        return convertToDto(imageRepository.save(imageDTO.convertToModel()));
    }

    @Override
    public ImageDTO update(Long id, ImageDTO imageDTO) {
        Image image = getModel(imageDTO.getId());
        image.setPath(imageDTO.getPath());
        image.setFacility(imageDTO.getFacility());
        image.setUser(imageDTO.getUser());
        return convertToDto(imageRepository.save(image));
    }

    @Override
    public void delete(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public void handleImageCreation(Image image, FacilityDTO facilityDTO, UserDTO userDTO) {
        try {
            if(facilityDTO != null) {image.setFacility(facilityDTO.convertToModel());}
            if(userDTO != null) {image.setUser(userDTO.convertToModel());}

            String[] parts = image.getPath().split("\\\\|\\\\\\\\");
            String filename = parts[parts.length - 1];
            image.setPath(filename);

            String fullPath = Paths.get("/sitpass-images", image.getPath()).toString();
            Path path = Paths.get(fullPath);
            try (FileInputStream imageStream = new FileInputStream(fullPath)) {
                String contentType = Files.probeContentType(path);

                fileService.uploadFile(imageStream, image.getPath(), contentType);
                create(convertToDto(image));
            } catch (IOException e) {
                throw new BadRequestException("Processing image file problem.");
            }
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }
}
