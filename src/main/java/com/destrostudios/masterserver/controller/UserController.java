package com.destrostudios.masterserver.controller;

import com.destrostudios.masterserver.controller.model.Credentials;
import com.destrostudios.masterserver.controller.model.Registration;
import com.destrostudios.masterserver.controller.model.UserDTO;
import com.destrostudios.masterserver.controller.model.UserDTOMapper;
import com.destrostudios.masterserver.database.UserSessionRepository;
import com.destrostudios.masterserver.database.schema.User;
import com.destrostudios.masterserver.database.schema.UserSession;
import com.destrostudios.masterserver.database.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private SessionService sessionService;
    private UserDTOMapper userDTOMapper = new UserDTOMapper();

    @PostMapping("/register")
    public ResponseEntity<Void> prepareRegistration(@RequestBody Registration registration) {
        Optional<User> existingUser = userRepository.findByLogin(registration.getLogin());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            String saltServer = BCrypt.gensalt(10);
            String hashedPassword = BCrypt.hashpw(registration.getHashedPassword(), saltServer);
            User user = User.builder()
                    .login(registration.getLogin())
                    .saltClient(registration.getSaltClient())
                    .saltServer(saltServer)
                    .hashedPassword(hashedPassword)
                    .build();
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/saltClient")
    public ResponseEntity<String> getSaltClient(@RequestBody String login) {
        Optional<String> saltClientOptional = userRepository.findSaltClientByLogin(login);
        if (saltClientOptional.isPresent()) {
            return ResponseEntity.ok(saltClientOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Credentials credentials) {
        Optional<User> userOptional = userRepository.findByLogin(credentials.getLogin());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String hashedPassword = BCrypt.hashpw(credentials.getHashedPassword(), user.getSaltServer());
            if (hashedPassword.equals(user.getHashedPassword())) {
                final String sessionId = UUID.randomUUID().toString();
                UserSession userSession = UserSession.builder()
                        .id(sessionId)
                        .user(user)
                        .startTime(LocalDateTime.now())
                        .build();
                userSessionRepository.save(userSession);
                return ResponseEntity.ok(sessionId);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/bySession")
    public ResponseEntity<UserDTO> getSessionUser(@RequestHeader String sessionId) {
        User user = sessionService.getUser(sessionId);
        if (user != null) {
            UserDTO userDTO = userDTOMapper.map(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
