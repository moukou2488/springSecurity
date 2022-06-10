package com.example.spot.web.user.dto;

import com.example.spot.common.enums.Gender;
import com.example.spot.common.enums.Role;
import com.example.spot.common.enums.Team;
import com.example.spot.common.model.Agreement;
import com.example.spot.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class UserRequest {

    @NotNull
    @Column(unique = true)
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Size(min = 2, max = 12)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9]{2,12}$")
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate birth;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Team team;

    private boolean promotion;

    public User toEntity(String encodingPass) {
        Agreement agreement = Agreement.builder()
                .age(true)
                .spotTerm(true)
                .privacy(true)
                .promotion(promotion)
                .build();

        return User.builder()
                .email(email)
                .password(encodingPass)
                .nickname(nickname)
                .name(name)
                .birth(birth)
                .gender(gender)
                .team(team)
                .agreement(agreement)
                .role(Role.USER)
                .build();
    }
}
