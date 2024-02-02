package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.User;
import ru.cft.template.entity.Wallet;
import ru.cft.template.mapper.UserMapper;
import ru.cft.template.model.request.LoginBody;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.response.TokenResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.model.request.UserUpdateBody;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.utils.JwtTokenUtils;

import java.util.Objects;
import java.util.UUID;

import static java.util.regex.Pattern.matches;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final JwtTokenUtils jwtTokenUtils;

    public TokenResponse loginUser(LoginBody body){
        User user = userRepository.findByPhone(body.phone())
                .filter(u -> Objects.equals(body.password(), u.getPassword()))
                .orElse(null);

        if (user == null){
            throw new UsernameNotFoundException("Invalid login details");
        }

        return TokenResponse.builder()
                .token(jwtTokenUtils.generateToken(user))
                .build();
    }


    public TokenResponse registerUser(RegisterBody body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        Wallet newWallet = walletService.createWallet();
        user.setWallet(newWallet);
        userRepository.save(user);

        return TokenResponse.builder()
                .token(jwtTokenUtils.generateToken(user))
                .build();
    }

    public UserResponse getUserResponseById(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        return UserMapper.mapUserToResponse(user);
    }

    public User getUserById(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }


    public UserResponse updateUser(Authentication authentication, UserUpdateBody body){
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        if (body.firstName() != null) {
            user.setFirstName(body.firstName());
        }
        if (body.lastName() != null) {
            user.setLastName(body.lastName());
        }
        if (body.email() != null) {
            user.setEmail(body.email());
        }

        userRepository.save(user);
        return UserMapper.mapUserToResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }
}
