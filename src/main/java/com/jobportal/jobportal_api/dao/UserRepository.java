package com.jobportal.jobportal_api.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.jobportal_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByUserId(String userId);
}
