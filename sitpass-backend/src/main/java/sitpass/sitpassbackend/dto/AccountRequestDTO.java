package sitpass.sitpassbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sitpass.sitpassbackend.model.AccountRequest;
import sitpass.sitpassbackend.model.enums.RequestStatus;

import java.time.LocalDate;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDTO {

    private Long id;
    @NotBlank
    private String email;

    @NotBlank
    private String password;
    private String rejectionReason;
    private LocalDate createdAt;
    @NotBlank
    private String address;
    private RequestStatus requestStatus;

    public static AccountRequestDTO convertToDto(AccountRequest accountRequest) {
        return AccountRequestDTO.builder()
                .id(accountRequest.getId())
                .email(accountRequest.getEmail())
                .password(accountRequest.getPassword())
                .rejectionReason(Optional.ofNullable(accountRequest.getRejectionReason()).orElse(null))
                .createdAt(accountRequest.getCreatedAt())
                .address(accountRequest.getAddress())
                .requestStatus(accountRequest.getRequestStatus())
                .build();
    }

    public AccountRequest convertToModel() {
        return AccountRequest.builder()
                .id(getId())
                .email(getEmail())
                .password(getPassword())
                .rejectionReason(getRejectionReason())
                .createdAt(getCreatedAt())
                .address(getAddress())
                .requestStatus(getRequestStatus())
                .build();
    }

}
