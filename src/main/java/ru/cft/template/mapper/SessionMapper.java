package ru.cft.template.mapper;

import ru.cft.template.entity.Session;
import ru.cft.template.model.response.CurrentSessionResponse;
import ru.cft.template.model.response.SessionResponse;

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
