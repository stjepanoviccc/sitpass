package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.AccountRequestDTO;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.model.AccountRequest;
import sitpass.sitpassbackend.model.enums.RequestStatus;
import sitpass.sitpassbackend.repository.RegistrationRepository;
import sitpass.sitpassbackend.service.EmailService;
import sitpass.sitpassbackend.service.RegistrationService;
import sitpass.sitpassbackend.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static sitpass.sitpassbackend.dto.AccountRequestDTO.convertToDto;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AccountRequestDTO> findAllPending() {
        List<AccountRequest> accountRequests = registrationRepository.findAllPending();
        return accountRequests.stream()
                .map(AccountRequestDTO::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountRequestDTO sendAccountRequest(AccountRequestDTO accountRequestDTO) {
        registrationRepository.findByEmail(accountRequestDTO.getEmail())
                .ifPresent(accountRequest -> {
                    throw new BadRequestException(String.format("Account with email %s is already in the database", accountRequestDTO.getEmail()));
                });

        accountRequestDTO.setRequestStatus(RequestStatus.PENDING);
        accountRequestDTO.setCreatedAt(LocalDate.now());
        return convertToDto(registrationRepository.save(accountRequestDTO.convertToModel()));
    }

    @Override
    public AccountRequestDTO handleAccountRequest(AccountRequestDTO accountRequestDTO) {
        AccountRequest existingAccountRequest = registrationRepository.findById(accountRequestDTO.getId())
                .orElseThrow(() -> new BadRequestException("AccountRequest not found with id: " + accountRequestDTO.getId()));
        existingAccountRequest.setRequestStatus(accountRequestDTO.getRequestStatus());
        String rStatus = existingAccountRequest.getRequestStatus().toString();
        if(rStatus.equals("REJECTED")) {
            existingAccountRequest.setRejectionReason(accountRequestDTO.getRejectionReason());
        }
        AccountRequest savedAccountRequest = registrationRepository.save(existingAccountRequest);
        if(rStatus.equals("ACCEPTED")) {
            UserDTO userDTO = new UserDTO();
            userDTO.setCreatedAt(LocalDate.now());
            userDTO.setEmail(savedAccountRequest.getEmail());
            userDTO.setPassword(passwordEncoder.encode(savedAccountRequest.getPassword()));
            userDTO.setAddress(savedAccountRequest.getAddress());
            userService.create(userDTO);
            emailService.sendEmail(userDTO.getEmail(), "Account request accepted", "Your account request has been accepted.");
        }
        return convertToDto(savedAccountRequest);
    }
}
