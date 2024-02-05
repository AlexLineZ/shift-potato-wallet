package ru.cft.template.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import ru.cft.template.model.request.LoginBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;

import java.util.List;

public interface SessionService {
    SessionResponse createSession(LoginBody body);

    List<CurrentSessionResponse> getAllSessions(Authentication authentication);

    CurrentSessionResponse getCurrentSession(Authentication authentication);

    ResponseEntity<?> deleteSessionById(Authentication authentication, String id);
}
