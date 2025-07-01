package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String userId);

    @Query("SELECT new com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO(" +
            "u.id, u.name, u.role, u.status) " +
            "FROM User u")
    List<ViewAllUsersResponseDTO> findAllUsers();
}
