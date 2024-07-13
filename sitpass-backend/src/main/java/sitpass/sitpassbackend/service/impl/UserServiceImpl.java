package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.AccountRequestDTO;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.AccountRequest;
import sitpass.sitpassbackend.model.Image;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.model.enums.RequestStatus;
import sitpass.sitpassbackend.repository.RegistrationRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.EmailService;
import sitpass.sitpassbackend.service.ImageService;
import sitpass.sitpassbackend.service.UserService;

import java.util.Optional;

import static sitpass.sitpassbackend.dto.UserDTO.convertToDTO;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageService imageService;
    private final EmailService emailService;
    private final RegistrationRepository registrationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getModel(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    String errorMessage = String.format("User with email %s not found.", email);
                    return new NotFoundException(errorMessage);
                });
    }

    @Override
    public User getModel(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = String.format("User with id %s not found.", id);
                    return new NotFoundException(errorMessage);
                });
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        Optional<AccountRequest> accountRequestOptional = registrationRepository.findByEmail(userDTO.getEmail());

        if (accountRequestOptional.isEmpty()) {
            String errorMessage = "No account request found for the given email.";
            throw new NotFoundException(errorMessage);
        }

        AccountRequestDTO accountRequestDTO = AccountRequestDTO.convertToDto(accountRequestOptional.get());
        if (accountRequestDTO.getRequestStatus() == RequestStatus.PENDING ||
                accountRequestDTO.getRequestStatus() == RequestStatus.REJECTED) {
            String errorMessage = String.format("Account Request Status: %s", accountRequestDTO.getRequestStatus());
            throw new BadRequestException(errorMessage);
        }

        return convertToDTO(userRepository.save(userDTO.convertToModel()));
    }

    @Override
    public UserDTO update(UserDTO userDTO, String imageUrl) {
        User existingUser = getModel(userDTO.getId());

        existingUser.setAddress(userDTO.getAddress());
        existingUser.setCity(userDTO.getCity());
        existingUser.setBirthday(userDTO.getBirthday());
        existingUser.setName(userDTO.getName());
        existingUser.setSurname(userDTO.getSurname());
        existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        existingUser.setZipCode(userDTO.getZipCode());

        User updatedUser = userRepository.save(existingUser);

        if(!imageUrl.isEmpty()) {
            // deleting one that exists(if exists)
            Optional<Image> image = imageService.findActiveByUser(existingUser.getEmail());
            image.ifPresent(value -> imageService.delete(value.getId()));
            // creating new
            Image newImage = new Image();
            newImage.setPath(imageUrl);
            imageService.handleImageCreation(newImage, null, convertToDTO(existingUser));
        }

        return convertToDTO(updatedUser);
    }

    @Override
    public UserDTO update(Long id, String password) {
        User existingUser = getModel(id);
        existingUser.setPassword(passwordEncoder.encode(password));
        User updatedUser  = userRepository.save(existingUser);
        emailService.sendEmail(existingUser.getEmail(), "Password Updated", "Your password has been updated");
        return convertToDTO(updatedUser);
    }

    @Override
    public Boolean validateCurrentPassword(Long id, String typedCurrentPassword) {
        User existingUser = getModel(id);
        String storedPassword = existingUser.getPassword();
        return passwordEncoder.matches(typedCurrentPassword, storedPassword);
    }

}
