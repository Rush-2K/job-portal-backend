package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.AuthRequestDto;
import com.jobportal.jobportal_api.dto.request.SignUpRequestDTO;
import com.jobportal.jobportal_api.dto.response.AuthResponseDto;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO user) {
        userService.registerUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> loginUser(@RequestBody AuthRequestDto authRequest) {
        // check for email
        log.info("login STARTED");
        AuthResponseDto authResponseDto = userService.login(authRequest);
        log.info("login ENDED");

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), authResponseDto));
    }
}
