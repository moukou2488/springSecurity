package com.example.spot.global.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsService customUserDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();
        CustomUserDetail userDetail = customUserDetailsService.loadUserByUsername(email);

        if (userDetail == null) {
            throw new BadCredentialsException("email is not found. username=" + email);
        }

        if (!this.passwordEncoder.matches(password, userDetail.getPassword())) {
            throw new BadCredentialsException("password is not matched");
        }
        return new UsernamePasswordAuthenticationToken(email, password);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
