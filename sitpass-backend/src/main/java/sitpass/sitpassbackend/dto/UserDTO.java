package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.User;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    private String name;
    private String surname;
    private LocalDate createdAt;
    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private String city;
    private String zipCode;
    private Boolean isDeleted;

    public static UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .createdAt(user.getCreatedAt())
                .phoneNumber(user.getPhoneNumber())
                .birthday(user.getBirthday())
                .address(user.getAddress())
                .city(user.getCity())
                .zipCode(user.getZipCode())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    public User convertToModel() {
        return User.builder()
                .id(getId())
                .email(getEmail())
                .password(getPassword())
                .name(getName())
                .surname(getSurname())
                .createdAt(getCreatedAt())
                .phoneNumber(getPhoneNumber())
                .birthday(getBirthday())
                .address(getAddress())
                .city(getCity())
                .zipCode(getZipCode())
                .isDeleted(getIsDeleted())
                .build();
    }

}
