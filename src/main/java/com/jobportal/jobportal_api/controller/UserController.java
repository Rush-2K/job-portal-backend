package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.request.DeleteUserDTO;
import com.jobportal.jobportal_api.dto.request.UpdateUserProfileRequestDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/listalluser")
    public ResponseEntity<?> allUser() {
        List<User> users = userService.getAllUser();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), users));

    }

    // @GetMapping("/userdetails")
    // public ResponseEntity<?> viewUserDetails() {
    // String email = userService
    // }

    @GetMapping("/viewuserdetails")
    public ResponseEntity<?> getUserDetails() {
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), userProfileResponseDTO));
    }

    // update user profile
    @PatchMapping("/updateprofile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UpdateUserProfileRequestDTO updateUserProfileRequestDTO) {
        // get the userid inside the token
        UserProfileResponseDTO userProfileResponseDTO = userService.getUserDetails();
        String id = userProfileResponseDTO.getUserId();

        // get the user profile
        User user = userService.getUserById(id);

        String updatedName = updateUserProfileRequestDTO.getName();
        String updatedEmail = updateUserProfileRequestDTO.getEmail();
        user.setUpdatedTime(LocalDateTime.now());

        if (updatedName != null) {
            user.setName(updatedName);
        }
        if (updatedEmail != null) {
            user.setEmail(updatedEmail);
        }

        // set and save the user
        UserProfileResponseDTO userProfileResponseDTO2 = userService.updateUserProfile(user);

        // return
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), userProfileResponseDTO2));
    }

    @DeleteMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserDTO deleteUserDTO) {
        userService.deleteUserById(deleteUserDTO.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                ApiStatus.SUCCESS.name(), LocalDateTime.now(), null));
    }

}
