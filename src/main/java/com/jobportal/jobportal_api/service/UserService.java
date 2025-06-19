package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.jwt.JwtUtils;
import com.jobportal.jobportal_api.jwt.UserPrincipal;
import com.jobportal.jobportal_api.mapper.AuthMapper;
import com.jobportal.jobportal_api.mapper.UpdateProfileMapper;
import com.jobportal.jobportal_api.mapper.UserDetailsMapper;

import lombok.extern.slf4j.Slf4j;

import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.request.AuthRequestDto;
import com.jobportal.jobportal_api.dto.response.AuthResponseDto;
import com.jobportal.jobportal_api.dto.response.UserProfileResponseDTO;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthMapper authMapper;
    private final UserDetailsMapper userDetailsMapper;
    private final UpdateProfileMapper updateProfileMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtils jwtUtils, AuthMapper authMapper,
            UserDetailsMapper userDetailsMapper, UpdateProfileMapper updateProfileMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.authMapper = authMapper;
        this.userDetailsMapper = userDetailsMapper;
        this.updateProfileMapper = updateProfileMapper;
    }

    public void registerUser(User user) {
        // Check for duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // Encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserId(UUID.randomUUID().toString());
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        // save the user
        User savedUser = userRepository.save(user);

        // Query and log the user from the database
        User fetchedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not saved!"));
        logger.info("User registered successfully and fetched from DB: {}", fetchedUser.getEmail());

    }

    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        // check for email
        User user = userRepository.findByEmail(authRequestDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // UsernamePasswordAuthenticationToken holds the credentials
        // authenticationManager.authenticate(...) triggers:
        // UserDetailsService.loadUserByUsername(email) to fetch the user.
        // Password check (e.g., matches with BCrypt-hashed password).
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword()));

        // After successful authentication, Spring Security returns an Authentication
        // object.
        // getPrincipal() returns the authenticated user object, casted to UserDetails.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Generates a JWT token based on the authenticated user’s info
        String jwtToken = jwtUtils.generateToken(user.getEmail(), user.getUserId(), user.getRole());

        // Object data = Map.of(
        // "token", jwtToken,
        // "email", user.getEmail(),
        // "username", user.getName(),
        // "userId", user.getId());

        return authMapper.toAuthResponseDto(user, jwtToken);
    }

    // @Override
    // public UserDetails loadUserByUsername(String email) {
    // User user = userRepository.findByEmail(email)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // return org.springframework.security.core.userdetails.User
    // .withUsername(user.getEmail())
    // .password(user.getPassword()) // hashed
    // .roles(user.getRole()) // adds "ROLE_" prefix automatically
    // .build();
    // }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public UserProfileResponseDTO getUserDetails() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String userId = principal.getUserId();
        log.info("User Id: {}", userId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            log.warn("No authentication in context – did the JWT filter run?");
        } else {
            log.info("Auth principal class: {}", auth.getPrincipal().getClass());
        }

        User user = userRepository.findByUserId(userId);
        log.info("User Data: {}", user);

        return userDetailsMapper.toUserProfileResponseDTO(user);
    }

    public UserProfileResponseDTO updateUserProfile(User user) {
        User updatedUser = userRepository.save(user);

        return updateProfileMapper.toUserProfileResponseDTO(updatedUser);
    }

    public User getUserById(String id) {
        return userRepository.findByUserId(id);
    }

}
