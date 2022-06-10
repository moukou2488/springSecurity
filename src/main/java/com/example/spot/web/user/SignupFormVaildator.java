package com.example.spot.web.user;

import com.example.spot.domain.user.UserRepository;
import com.example.spot.web.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignupFormVaildator implements Validator {

    private final UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(UserRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRequest userRequest = (UserRequest)target;
        if (userRepository.existsByEmail(userRequest.getEmail())){
            errors.rejectValue("email", "invalid.email", new Object[]{userRequest.getEmail()}, "이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(userRequest.getNickname())){
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{userRequest.getNickname()}, "이미 사용 중인 닉네임입니다.");
        }
    }
}
