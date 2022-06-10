package com.example.spot.domain.user;

import com.example.spot.common.enums.Gender;
import com.example.spot.common.enums.Role;
import com.example.spot.common.enums.Team;
import com.example.spot.common.model.Agreement;
import com.example.spot.common.model.BaseTime;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Table(name = "users")
@Entity
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
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

    @ColumnDefault("50")
    private int weather;

    @Embedded
    private Agreement agreement;

    @ColumnDefault("false")
    private boolean emailCertificated;

    private String emailCheckToken;

    @ColumnDefault("true")
    private boolean activated;

    @Enumerated(EnumType.STRING)
    private Role role;

    public void generateEamilCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
    }

    public void completeSignUp() {
        this.emailCertificated = true;
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
}
