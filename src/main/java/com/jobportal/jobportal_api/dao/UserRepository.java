package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {

        Optional<User> findByEmail(String email);

        Optional<User> findByUserId(String userId);

        @Query("SELECT new com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO(" +
                        "u.id, u.name, u.role, u.status) " +
                        "FROM User u")
        List<ViewAllUsersResponseDTO> findAllUsers();

        @Query("SELECT new com.jobportal.jobportal_api.dto.response.ViewAllUsersResponseDTO(" +
                        "u.id, u.name, u.role, u.status) " +
                        "FROM User u " +
                        "WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                        "AND (:id IS NULL OR u.id = :id) " +
                        "AND (:status IS NULL OR u.status = :status)")
        Page<ViewAllUsersResponseDTO> filterUsers(
                        @Param("name") String name,
                        @Param("id") Long id,
                        @Param("status") UserStatus status,
                        Pageable pageable);

}
