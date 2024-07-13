package sitpass.sitpassbackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sitpass.sitpassbackend.exception.NotFoundException;
import sitpass.sitpassbackend.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found.", email)));
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
