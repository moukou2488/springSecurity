package com.example.spot.common.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Email {
    private String to;
    private String subject;
    private String message;
}
