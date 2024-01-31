package ru.cft.template.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.RegisterBody;
import ru.cft.template.model.response.TokenResponse;
import ru.cft.template.model.response.UserResponse;
import ru.cft.template.model.request.UserUpdateBody;
import ru.cft.template.service.impl.UserService;

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
