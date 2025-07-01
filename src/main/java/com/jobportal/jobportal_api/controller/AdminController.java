package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/viewall")
    public ResponseEntity<?> viewAllUsers() {
        List<ViewAllUsersResponseDTO> response = adminService.listAllUser();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "Get All users success", LocalDateTime.now(), response));
    }

}
