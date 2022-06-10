package com.example.spot.common.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Agreement {
    @Column(name = "agreement_age")
    private boolean age;

    @Column(name = "agreement_privacy")
    private boolean privacy;

    @Column(name = "agreement_spot_term")
    private boolean spotTerm;

    @Column(name = "agreement_promotion")
    private boolean promotion;
}
