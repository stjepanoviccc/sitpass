package sitpass.sitpassbackend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import sitpass.sitpassbackend.dto.UserDTO;
import sitpass.sitpassbackend.exception.BadRequestException;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.model.AccountRequest;
import sitpass.sitpassbackend.model.User;
import sitpass.sitpassbackend.model.enums.RequestStatus;
import sitpass.sitpassbackend.repository.RegistrationRepository;
import sitpass.sitpassbackend.repository.UserRepository;
import sitpass.sitpassbackend.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static sitpass.sitpassbackend.dto.UserDTO.convertToDTO;
import static sitpass.sitpassbackend.model.enums.RequestStatus.ACCEPTED;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private final User user = createUser();
    private final AccountRequest accountRequest = createAccountRequest(RequestStatus.PENDING);

    private User createUser() {
        return User.builder()
                .id(1L)
                .email("email@gmail.com")
                .password("password")
                .name("Andrej")
                .surname("Stjepanovic")
                .createdAt(LocalDate.now())
                .phoneNumber("066111999")
                .birthday(LocalDate.of(2000,11,22))
                .address("Bulevar 1")
                .city("Novi Sad")
                .zipCode("21001")
                .isDeleted(false)
                .build();
    }

    private AccountRequest createAccountRequest(RequestStatus requestStatus) {
        return AccountRequest.builder()
                .id(1L)
                .email("email@gmail.com")
                .password("password")
                .rejectionReason(null)
                .createdAt(LocalDate.now())
                .address("Bulevar 1")
                .requestStatus(requestStatus)
                .build();
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetUser_whenGetModelById_ifUserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDTO userDTO = convertToDTO(userService.getModel(user.getId()));

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenGetModelById_ifUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(userRepository).findById(user.getId());
    }

    @Test
    void shouldGetUser_whenGetModelByEmail_ifUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDTO userDTO = convertToDTO(userService.getModel(user.getEmail()));

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void shouldThrowNotFoundException_whenGetModelByEmail_ifUserDoesNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getModel(user.getEmail()));
        assertEquals(String.format("User with email %s not found.", user.getEmail()), exception.getMessage());

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    void shouldCreateUser_whenCreate_ifAccountRequestIsAccepted() {
        accountRequest.setRequestStatus(ACCEPTED);

        when(registrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(accountRequest));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO createdUserDTO = userService.create(convertToDTO(user));

        assertEquals(createdUserDTO.getId(), user.getId());
        assertEquals(createdUserDTO.getEmail(), user.getEmail());

        verify(registrationRepository).findByEmail(user.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowBadRequestException_whenCreate_ifAccountRequestIsNotAccepted() {
        // rejected or pending
        accountRequest.setRequestStatus(RequestStatus.REJECTED);
        when(registrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(accountRequest));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.create(convertToDTO(user)));
        assertEquals(String.format("Account Request Status: %s", accountRequest.getRequestStatus()), exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundException_whenCreate_ifAccountRequestDoesNotExist() {
        when(registrationRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.create(convertToDTO(user)));
        assertEquals("No account request found for the given email.", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldUpdateUser_whenUpdate_ifUserExists() {
        String imgUrl = "random_new.jpg";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedUserDTO = userService.update(convertToDTO(user), imgUrl);

        assertEquals(user.getId(), updatedUserDTO.getId());
        assertEquals(user.getName(), updatedUserDTO.getName());
        assertEquals(user.getSurname(), updatedUserDTO.getSurname());
        assertEquals(user.getAddress(), updatedUserDTO.getAddress());
        assertEquals(user.getCity(), updatedUserDTO.getCity());
        assertEquals(user.getBirthday(), updatedUserDTO.getBirthday());
        assertEquals(user.getPhoneNumber(), updatedUserDTO.getPhoneNumber());
        assertEquals(user.getZipCode(), updatedUserDTO.getZipCode());
        assertEquals(imgUrl, "random_new.jpg");

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdate_ifUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(userRepository).findById(user.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldUpdateUserPassword_whenUpdatePassword_ifUserExist() {
        String newPassword = "newPassword";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updatedUserDTO = userService.update(user.getId(), newPassword);

        assertEquals(user.getId(), updatedUserDTO.getId());

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowNotFoundException_whenUpdatePassword_ifUserDoesNotExist() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        notFoundByIdAssertion();

        verify(userRepository).findById(user.getId());
        verify(userRepository, never()).save(any(User.class));
    }

    // helping
    void notFoundByIdAssertion() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getModel(user.getId()));
        assertEquals(String.format("User with id %s not found.", user.getId()), exception.getMessage());
    }
}