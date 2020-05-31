package com.destrostudios.masterserver.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.destrostudios.masterserver.database.schema.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/authToken")
public class AuthenticationController {

    @Autowired
    public AuthenticationController(KeyService keyService, SessionService sessionService, @Value("${keysDirectory}") String appsDirectory) {
        this.sessionService = sessionService;
        KeyPair keyPair = keyService.readKeyPair("RSA", appsDirectory + "private.der", appsDirectory + "public.pem");
        algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        verifier = JWT.require(algorithm).build();
    }
    private SessionService sessionService;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    @GetMapping
    public ResponseEntity<String> getAuthToken(@RequestHeader String sessionId) {
        User user = sessionService.getUser(sessionId);
        if (user != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("login", user.getLogin());

            String token = JWT.create()
                    .withIssuedAt(new Date())
                    .withClaim("user", userMap)
                    .sign(algorithm);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/verify")
    public boolean verifyAuthToken(@RequestParam String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }
}
