package ru.cft.template.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.cft.template.model.TokenResponse;

@Service
@Data
public class UserService {

    public TokenResponse registerUser() {
        return TokenResponse.builder()
                .build();
    }
}
