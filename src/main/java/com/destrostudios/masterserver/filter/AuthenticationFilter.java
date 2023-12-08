package com.destrostudios.masterserver.filter;

import com.destrostudios.masterserver.service.AuthTokenService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthenticationFilter implements Filter {

    @Autowired
    private AuthTokenService authTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authorization = httpRequest.getHeader("Authorization");
        if (authorization != null) {
            String authToken = authorization.substring("Bearer ".length());
            Map<String, Object> claims = authTokenService.verifyToken(authToken);
            Map<String, Object> userClaims = (Map<String, Object>) claims.get("user");
            int userId = (int) userClaims.get("id");
            httpRequest.setAttribute("userId", userId);
        }
        chain.doFilter(request, response);
    }
}
