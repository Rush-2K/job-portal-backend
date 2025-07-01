package com.jobportal.jobportal_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.dtos.PagedResponseDTO;
import com.jobportal.jobportal_api.enums.UserStatus;

@Service
public class AdminService {

    private UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
