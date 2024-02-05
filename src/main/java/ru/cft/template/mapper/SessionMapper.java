package ru.cft.template.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.entity.Session;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;

@Component
public class SessionMapper {
    public static SessionResponse mapSessionToResponse(Session session){
        return new SessionResponse(
                session.getId(),
                session.getUserId(),
                session.getToken(),
                session.getExpirationTime()
        );
    }

    public static CurrentSessionResponse mapSessionToResponse(Session session, Boolean active){
        return new CurrentSessionResponse(
                session.getId(),
                session.getUserId(),
                session.getExpirationTime(),
                active
        );
    }
}
