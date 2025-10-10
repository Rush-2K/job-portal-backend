package com.jobportal.jobportal_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dto.response.EmailResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    public JavaMailSender mailSender;

    // use for testing
    public void sendInactivityReminder(String toEmail) {
        // For now just log
        log.info("Sending inactivity reminder to: " + toEmail);
    }

    public EmailResponseDTO sendEmail(String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("phantomthief1113@gmail.com"); // same as configured
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);

            EmailResponseDTO emailResponseDTO = new EmailResponseDTO();

            String msg = "Email sent successfully to " + to;

            emailResponseDTO.setRecipient(to);
            emailResponseDTO.setMessage(msg);

            logger.info("Email sent successfully to {}", to);
            return emailResponseDTO;

        } catch (Exception e) {

            EmailResponseDTO emailResponseDTO = new EmailResponseDTO();

            String msg = "Email failed sent to " + to;

            emailResponseDTO.setRecipient(to);
            emailResponseDTO.setMessage(msg);

            logger.info("Email sent successfully to {}", to);
            return emailResponseDTO;
        }

    }

    public EmailResponseDTO sendInactiveUserReminder(String email, String name) {

        String subject = "Account Inactive";
        String text = "Dear " + name
                + ", due to inactivity for your account after a month, your account now has already been inactive";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("phantomthief1113@gmail.com"); // same as configured
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);

            EmailResponseDTO emailResponseDTO = new EmailResponseDTO();

            String msg = "Email sent successfully to " + email;

            emailResponseDTO.setRecipient(email);
            emailResponseDTO.setMessage(msg);

            logger.info("Email sent successfully to {}", email);
            return emailResponseDTO;

        } catch (Exception e) {

            EmailResponseDTO emailResponseDTO = new EmailResponseDTO();

            String msg = "Email failed sent to " + email;

            emailResponseDTO.setRecipient(email);
            emailResponseDTO.setMessage(msg);

            logger.info("Email sent successfully to {}", email);
            return emailResponseDTO;
        }

    }
}
