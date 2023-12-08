package com.destrostudios.masterserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.destrostudios.masterserver.database.schema.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthTokenService {

    @Autowired
    public AuthTokenService(KeyService keyService, @Value("${destrostudios.keys-directory}") String keysDirectory) {
        KeyPair keyPair = keyService.readKeyPair("RSA", keysDirectory + "private.der", keysDirectory + "public.pem");
        algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
        verifier = JWT.require(algorithm).build();
    }
    private Algorithm algorithm;
    private JWTVerifier verifier;

    public String signToken(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("login", user.getLogin());

        return JWT.create()
            .withIssuedAt(new Date())
            .withClaim("user", userMap)
            .sign(algorithm);
    }

    public Map<String, Object> verifyToken(String authToken) throws JWTVerificationException {
        DecodedJWT decodedJWT = verifier.verify(authToken);
        Map<String, Object> claims = new HashMap<>();
        claims.put("iat", decodedJWT.getClaim("iat").asInt());
        claims.put("user", decodedJWT.getClaim("user").asMap());
        return claims;
    }
}
