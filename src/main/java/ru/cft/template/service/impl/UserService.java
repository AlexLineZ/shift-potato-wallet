package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.User;
import ru.cft.template.mapper.UserMapper;
import ru.cft.template.model.RegisterBody;
import ru.cft.template.model.TokenResponse;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public TokenResponse registerUser(RegisterBody body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        userRepository.save(user);

        return TokenResponse.builder()
                .token(jwtTokenUtils.generateToken(user))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: $email not found"));
    }
}
