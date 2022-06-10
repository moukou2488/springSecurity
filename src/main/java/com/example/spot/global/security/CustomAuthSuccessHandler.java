package com.example.spot.global.security;

import com.example.spot.web.user.dto.UserView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Cookie jSession = new Cookie("JSESSIONID", request.getSession().getId());

        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(HttpStatus.OK.value());

        objectMapper.writeValue(response.getWriter(), jSession.getValue());

        Cookie rememberMe = new Cookie("rememberMe", request.getSession().getId());
    }
}
