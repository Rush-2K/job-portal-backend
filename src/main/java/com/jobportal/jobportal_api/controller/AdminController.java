package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.UpdateUserStatusRequestDTO;
import com.jobportal.jobportal_api.dto.response.UpdateUserStatusResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileDetailsAdminResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.dtos.PagedResponseDTO;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.enums.UserStatus;
import com.jobportal.jobportal_api.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewAllUsers() {
        List<ViewAllUsersResponseDTO> response = adminService.listAllUser();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "Get All users success", LocalDateTime.now(), response));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UserStatus status,
            Pageable pageable) {
        PagedResponseDTO<ViewAllUsersResponseDTO> data = adminService.filterUsers(id, name, status, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "Filter success", LocalDateTime.now(), data));
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<?> viewUserDetails(@PathVariable Long userId) {
        UserProfileDetailsAdminResponseDTO data = adminService.userDetails(userId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                data.getMessage(), LocalDateTime.now(), data));
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUserStatus(@RequestBody UpdateUserStatusRequestDTO request) {
        adminService.changeUserStatus(request);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                "User status updated successfully", LocalDateTime.now(), null));
    }

}
