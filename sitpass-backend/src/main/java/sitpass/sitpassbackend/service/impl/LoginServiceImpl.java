package sitpass.sitpassbackend.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.dto.LoginRequestDTO;
import sitpass.sitpassbackend.dto.LoginResponseDTO;
import sitpass.sitpassbackend.exception.UnauthorizedException;
import sitpass.sitpassbackend.service.LoginService;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .refreshToken(jwtService.generateRefreshToken(userDetails))
                    .build();
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Authentication failed. Please check your credentials.");
        }
    }

    @Override
    public LoginResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String refreshToken = authHeader.substring(7);

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));

        if (jwtService.isTokenValid(refreshToken, userDetails)) {
            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new UnauthorizedException("Refresh token is not valid");
        }
    }
}
