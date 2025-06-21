package com.jobportal.jobportal_api.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.jwt.AuthTokenFilter;

@Configuration
public class SecurityConfig {

    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter)
            throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configure(http))
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers("/").permitAll()
                        // .requestMatchers("/api").permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // Public endpoints for signup/login

                        // ADMIN
                        .requestMatchers("/api/user/listalluser").hasRole("ADMIN")
                        .requestMatchers("/api/user/deleteuser").hasRole("ADMIN")
                        // EMPLOYER
                        .requestMatchers("/api/jobmgmt/createpost").hasRole("EMPLOYER")
                        .requestMatchers("/api/jobmgmt/viewjobs").hasRole("EMPLOYER")
                        // JOB SEEKER
                        .requestMatchers("/api/user/viewuserdetails").hasRole("JOB_SEEKER")
                        .requestMatchers("/api/user/updateprofile").hasRole("JOB_SEEKER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless session
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // add/use JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // strength defaults to 10
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            logger.info("User email: {}", email);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email" + email));

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };

    }

}
