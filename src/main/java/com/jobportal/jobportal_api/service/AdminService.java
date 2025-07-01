package com.jobportal.jobportal_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;

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

}
