package com.example.spot.domain.mail;

import com.example.spot.common.model.Email;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(Email email);
}
