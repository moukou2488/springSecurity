package com.example.spot.web.user;

import com.example.spot.domain.user.UserService;
import com.example.spot.web.user.dto.UserRequest;
import com.example.spot.web.user.dto.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final SignupFormVaildator signupFormVaildator;

    @PostMapping("")
    public ResponseEntity signup(@Valid @RequestBody UserRequest userRequest, Errors errors) {

        signupFormVaildator.validate(userRequest, errors);

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(UserView.toUserView(userService.saveUser(userRequest)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity checkAllowedEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.vaildEmail(email));
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity checkAllowedNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.vaildNickname(nickname));
    }

    @GetMapping("/email-certificated")
    public String checkEmailToken(String emailToken, String email, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        return userService.checkEmail(emailToken, email, request, response, session);
    }

    @PatchMapping("/email-token")
    public void resendSignupEmail(String email){
        userService.resetSignupEmail(email);
    }

}
