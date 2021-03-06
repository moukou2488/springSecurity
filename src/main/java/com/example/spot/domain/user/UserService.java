package com.example.spot.domain.user;

import com.example.spot.common.enums.ReturnMessage;
import com.example.spot.common.model.Email;
import com.example.spot.common.words.CheckWordList;
import com.example.spot.domain.mail.EmailService;
import com.example.spot.web.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;


    @Transactional
    public User saveUser(UserRequest userRequest) {
        User newUser = userRepository.save(
                userRequest.toEntity(passwordEncoder.encode(userRequest.getPassword()))
        );
        newUser.generateEamilCheckToken();

        sendSignupConfirmEmail(newUser);

        return newUser;
    }

    private void sendSignupConfirmEmail(User user) {
        String emailCheckToken = user.getEmailCheckToken();
        String userEmail = user.getEmail();

        Context context = new Context();

        context.setVariable("link", "/users/email-certificated?emailToken=" + emailCheckToken + "&email=" + userEmail);
        context.setVariable("host", "http://localhost:8080");

        String message = templateEngine.process("confirm-email", context);

        Email email = Email.builder()
                .to(userEmail)
                .subject("SPOT BUDDY ?????? ?????? ???????????????. (1?????? ??????)")
                .message(message)
                .build();

        emailService.sendEmail(email);
    }

    @Transactional
    public void resetSignupEmail(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + "???????????? ?????? ??????????????????."));
        user.generateEamilCheckToken();

        sendSignupConfirmEmail(user);
    }

    public Map<String, String> vaildEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            return ReturnMessage.apiReturnMessageSetting(ReturnMessage.CANT);
        } else return ReturnMessage.apiReturnMessageSetting(ReturnMessage.CAN);
        //TODO :: ???????????? ?????? ?????? ??????
    }

    public Map<String, String> vaildNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            return ReturnMessage.apiReturnMessageSetting(ReturnMessage.CANT);
        } else if (CheckWordList.badWordsConfirm(nickname)) {
            return ReturnMessage.apiReturnMessageSetting(ReturnMessage.BAD_WORD);
        } else return ReturnMessage.apiReturnMessageSetting(ReturnMessage.CAN);
    }

    @Transactional
    public String checkEmail(String emailToken, String email, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + "???????????? ?????? ??????????????????."));
        if (!user.isValidToken(emailToken)) return "????????? ????????? ???????????? ????????????.";

        session.invalidate();

        int betweenSignupTime = Duration.between(user.getCreatedAt(), LocalDateTime.now()).toHoursPart();

        if (betweenSignupTime < 1) {
            user.completeSignUp();

            String userAgent = request.getHeader("User-Agent");

            // pc user??? ??? web redirect
            if (!userAgent.contains("Mobile")) {
                response.sendRedirect("http://www.spotbuddy.co.kr/");
                return "PC User";
            }

            // ios app??? ????????? ?????? app?????? ??????
            if (userAgent.contains("iPhone")) {
                response.sendRedirect("spot://path/deeplink");
                return "IOS User";
            }

            response.sendRedirect("http://www.spotbuddy.co.kr/");

            return "success";
        } else return "?????? ????????? ?????????????????????.";

    }
}
