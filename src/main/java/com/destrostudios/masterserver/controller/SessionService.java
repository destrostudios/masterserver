package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.database.UserSessionRepository;
import com.destrostudios.masterserver.database.schema.User;
import com.destrostudios.masterserver.database.schema.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    User getUser(String sessionId) {
        Optional<UserSession> userSession = userSessionRepository.findById(sessionId);
        return userSession.map(UserSession::getUser).orElse(null);
    }
}
