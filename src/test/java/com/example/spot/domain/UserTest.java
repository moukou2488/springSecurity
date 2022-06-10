package com.example.spot.domain;

import com.example.spot.common.enums.Gender;
import com.example.spot.common.enums.Role;
import com.example.spot.common.enums.Team;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

public class UserTest {
    @Test
    void userAgreement(){
        assertThat("LG Twins").isEqualTo(Team.LG.getFullName());
        assertThat("FEMALE").isEqualTo(Gender.FEMALE.name());
        System.out.println("Role.USER.name() = " + Role.USER.getGrantedAuthority());
        assertThat("ROLE_USER").isEqualTo(Role.USER.getGrantedAuthority());
    }
}
