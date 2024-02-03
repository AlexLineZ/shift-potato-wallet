package ru.cft.template.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.cft.template.entity.BannedToken;
import ru.cft.template.entity.Session;
import ru.cft.template.entity.User;
import ru.cft.template.exception.AccessRightsException;
import ru.cft.template.mapper.SessionMapper;
import ru.cft.template.model.request.LoginBody;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;
import ru.cft.template.repository.BannedTokenRepository;
import ru.cft.template.repository.SessionRepository;
import ru.cft.template.repository.UserRepository;
import ru.cft.template.jwt.JwtTokenUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserService userService;
    private final SessionRepository sessionRepository;
    private final BannedTokenRepository bannedTokenRepository;

    public SessionResponse createSession(LoginBody body){
        User user = userRepository.findByPhone(body.phone())
                .filter(u -> Objects.equals(body.password(), u.getPassword()))
                .orElse(null);

        if (user == null){
            throw new UsernameNotFoundException("Invalid login details");
        }

        String token = jwtTokenUtils.generateToken(user);
        Date expiredDate = jwtTokenUtils.getExpirationDateFromToken(token);

        Session session = new Session();
        session.setUserId(user.getId());
        session.setToken(token);
        session.setExpirationTime(expiredDate);
        sessionRepository.save(session);

        return SessionMapper.mapSessionToResponse(session);
    }

    public List<CurrentSessionResponse> getAllSessions(Authentication authentication){
        User user = userService.getUserById(authentication);

        List<Session> sessions = sessionRepository.findByUserId(user.getId());
        Date now = new Date();
        return sessions.stream()
                .map(session -> {
                    boolean isActive = session.getExpirationTime().after(now);
                    return SessionMapper.mapSessionToResponse(session, isActive);
                })
                .collect(Collectors.toList());

    }

    public CurrentSessionResponse getCurrentSession(Authentication authentication){
        String currentToken = (String)authentication.getCredentials();

        Optional<Session> sessionOptional = sessionRepository.findByToken(currentToken);
        if (sessionOptional.isPresent()) {
            Session session = sessionOptional.get();
            Date now = new Date();
            return SessionMapper.mapSessionToResponse(
                    session,
                    jwtTokenUtils.getExpirationDateFromToken(currentToken).after(now)
            );
        } else {
           throw new UsernameNotFoundException("Session not found");
        }
    }

    public ResponseEntity<?> deleteSessionById(Authentication authentication, String id){
        UUID currentUserId = userService.getUserById(authentication).getId();
        UUID sessionId = UUID.fromString(id);

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (!session.getUserId().equals(currentUserId)) {
           throw new AccessRightsException("You can only delete your own sessions");
        }

        BannedToken bannedToken = new BannedToken();
        bannedToken.setToken(authentication.getCredentials().toString());

        bannedTokenRepository.save(bannedToken);
        sessionRepository.deleteById(sessionId);

        return ResponseEntity.ok().build();
    }
}
