package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.EmailRequestDTO;
import com.jobportal.jobportal_api.dto.response.EmailResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> sendTestEmail(@RequestBody EmailRequestDTO emailRequestDTO) {

        log.info("Starting to send email ~");

        EmailResponseDTO emailResponseDTO = emailService.sendEmail(emailRequestDTO.getRecipientEmail(),
                emailRequestDTO.getSubject(), emailRequestDTO.getBody());

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), emailResponseDTO));
    }
}
