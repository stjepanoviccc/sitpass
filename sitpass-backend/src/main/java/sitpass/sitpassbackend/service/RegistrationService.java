package sitpass.sitpassbackend.service;

import sitpass.sitpassbackend.dto.AccountRequestDTO;

import java.util.List;

public interface RegistrationService {

    List<AccountRequestDTO> findAllPending();
    AccountRequestDTO sendAccountRequest(AccountRequestDTO accountRequestDTO);
    AccountRequestDTO handleAccountRequest(AccountRequestDTO accountRequestDTO);

}
