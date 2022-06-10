package com.example.spot.global.security;

import com.example.spot.domain.user.User;
import com.example.spot.domain.user.UserRepository;
import com.example.spot.global.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public CustomUserDetail loadUserByUsername(String email){ //유저 정보를 권한 정보와 함께 가져옴
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + "존재하지 않는 이메일입니다."));

        return new CustomUserDetail(user);

    }

}
