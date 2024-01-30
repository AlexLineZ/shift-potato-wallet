package ru.cft.template.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cft.template.model.RegisterBody;
import ru.cft.template.model.TokenResponse;
import ru.cft.template.service.impl.UserService;

@RestController
@RequestMapping("api/users")
@Data
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterBody body){
        return ResponseEntity.ok(userService.registerUser(body));
    }
}
