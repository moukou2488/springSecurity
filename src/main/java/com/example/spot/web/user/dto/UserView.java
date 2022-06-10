package com.example.spot.web.user.dto;

import com.example.spot.common.enums.Gender;
import com.example.spot.common.enums.Team;
import com.example.spot.domain.user.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    private String email;

    private String nickname;

    private String name;

    private LocalDate birth;

    private Gender gender;

    private Team team;

    public static UserView toUserView(User user) {
        return UserView.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .name(user.getName())
                .birth(user.getBirth())
                .gender(user.getGender())
                .team(user.getTeam())
                .build();
    }
}
