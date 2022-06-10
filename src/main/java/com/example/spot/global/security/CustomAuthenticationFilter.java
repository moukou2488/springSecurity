package com.example.spot.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = true;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported : " + request.getMethod());
        }
        UsernamePasswordAuthenticationToken authRequest;

        String username;
        String password;

        if (request.getContentType().equals(MimeTypeUtils.APPLICATION_JSON_VALUE)) {
            //json Request인 경우
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Login login = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), Login.class);

                username = login.getEmail();
                password = login.getPassword();

                authRequest =
                        new UsernamePasswordAuthenticationToken(username, password);

            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type(application/json) Parsing Error");
            }

        } else {
            username = obtainUsername(request);
            password = obtainPassword(request);

            username = username.trim();

            username = (username != null) ? username : "";
            password = (password != null) ? password : "";

            authRequest =
                    new UsernamePasswordAuthenticationToken(username, password);
        }

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Getter
    @Setter
    static class Login{
        private String email;
        private String password;
        private boolean rememberMe;
    }

}
