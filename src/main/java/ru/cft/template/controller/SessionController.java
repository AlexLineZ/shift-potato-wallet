package ru.cft.template.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.cft.template.model.request.LoginBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;
import ru.cft.template.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @GetMapping("/sessions")
    public ResponseEntity<List<CurrentSessionResponse>> getAllSessions(Authentication authentication){
        return ResponseEntity.ok(sessionService.getAllSessions(authentication));
    }

    @GetMapping("/sessions/current")
    public ResponseEntity<CurrentSessionResponse> getCurrentSession(Authentication authentication){
        return ResponseEntity.ok(sessionService.getCurrentSession(authentication));
    }

    @PostMapping("/sessions")
    public ResponseEntity<SessionResponse> createSession(@RequestBody LoginBody body){
        return ResponseEntity.ok(sessionService.createSession(body));
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(Authentication authentication, @PathVariable String id){
        return ResponseEntity.ok(sessionService.deleteSessionById(authentication, id));
    }
}
