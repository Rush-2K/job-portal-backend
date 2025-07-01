package com.jobportal.jobportal_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.request.UpdateUserStatusRequestDTO;
import com.jobportal.jobportal_api.dto.response.UpdateUserStatusResponseDTO;
import com.jobportal.jobportal_api.dto.response.UserProfileDetailsAdminResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.dtos.PagedResponseDTO;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.UserStatus;
import com.jobportal.jobportal_api.mapper.UserProfileDetailsAdminMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminService {

    private UserRepository userRepository;
    private UserProfileDetailsAdminMapper userProfileDetailsAdminMapper;

    public AdminService(UserRepository userRepository, UserProfileDetailsAdminMapper userProfileDetailsAdminMapper) {
        this.userRepository = userRepository;
        this.userProfileDetailsAdminMapper = userProfileDetailsAdminMapper;
    }

    public List<ViewAllUsersResponseDTO> listAllUser() {
        List<ViewAllUsersResponseDTO> users = userRepository.findAllUsers();

        return users;
    }

    public PagedResponseDTO<ViewAllUsersResponseDTO> filterUsers(Long id, String name, UserStatus status,
            Pageable pageable) {
        Page<ViewAllUsersResponseDTO> pageResult = userRepository.filterUsers(name, id, status, pageable);
        PagedResponseDTO<ViewAllUsersResponseDTO> response = PagedResponseDTO.<ViewAllUsersResponseDTO>builder()
                .content(pageResult.getContent())
                .page(pageResult.getNumber())
                .size(pageResult.getSize())
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .last(pageResult.isLast())
                .build();

        return response;
    }

    public UserProfileDetailsAdminResponseDTO userDetails(Long userId) {
        // find user by userid
        User data = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // return using mapper
        UserProfileDetailsAdminResponseDTO result = userProfileDetailsAdminMapper
                .toUserProfileDetailsAdminResponseDTO(data);
        result.setMessage("Retrieve user details successfully");

        return result;

    }

    public void changeUserStatus(UpdateUserStatusRequestDTO request) {
        // get user and status
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // validation if status is same
        if (user.getStatus() == request.getStatus()) {
            throw new IllegalStateException("User already has the requested status");
        }
        // set new status and save
        user.setStatus(request.getStatus());
        userRepository.save(user);

    }

}
