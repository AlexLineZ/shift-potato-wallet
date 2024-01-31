package ru.cft.template.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.RegisterBody;
import ru.cft.template.model.TokenResponse;
import ru.cft.template.model.UserResponse;
import ru.cft.template.model.UserUpdateBody;
import ru.cft.template.service.impl.UserService;

import java.util.UUID;

@RestController
@RequestMapping("api/users")
@Data
public class UserController {
    private final UserService userService;

    @PostMapping("register")
    public ResponseEntity<TokenResponse> registerUser(@RequestBody RegisterBody body) {
        return ResponseEntity.ok(userService.registerUser(body));
    }

    @GetMapping("profile")
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserById(authentication));
    }

    @PatchMapping("update")
    public ResponseEntity<UserResponse> updateUser(Authentication authentication, @RequestBody UserUpdateBody body){
        return ResponseEntity.ok(userService.updateUser(authentication, body));
    }
}
