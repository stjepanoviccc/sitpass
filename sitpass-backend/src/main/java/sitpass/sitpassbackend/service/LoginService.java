package sitpass.sitpassbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sitpass.sitpassbackend.dto.LoginRequestDTO;
import sitpass.sitpassbackend.dto.LoginResponseDTO;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO request);

    LoginResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response);

}
