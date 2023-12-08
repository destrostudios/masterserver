package com.destrostudios.masterserver.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.destrostudios.masterserver.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authToken")
public class AuthTokenController {

    @Autowired
    private AuthTokenService authTokenService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyAuthToken(@RequestHeader String authToken) {
        try {
            return ResponseEntity.ok(authTokenService.verifyToken(authToken));
        } catch (JWTVerificationException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }
}
