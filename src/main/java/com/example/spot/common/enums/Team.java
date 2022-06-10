package com.example.spot.common.enums;

public enum Team {
    DOOSAN("Doosan Bears"),
    HANHWA("Hanwha Eagles"),
    NC("NC Dinos"),
    LOTTE("Lotte Giants"),
    KIWOOM("Kiwoom Heroes"),
    KIA("KIA Tigers"),
    KT("Kt Wiz"),
    SSG("SSG Landers"),
    SAMSUNG("Samsung Lions"),
    LG("LG Twins");

    private String fullName;

    Team(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
