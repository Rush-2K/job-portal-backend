package com.jobportal.jobportal_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.enums.UserStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InactiveUserScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // run once every one minute
    @Scheduled(cron = "0 */1 * * * *")
    public void notifyInactiveUsers() {
        List<User> inactiveUsers = userRepository.findByStatus(UserStatus.INACTIVE);

        for (User user : inactiveUsers) {
            emailService.sendInactiveUserReminder(user.getEmail(), user.getName());
        }

        log.info("Sent inactivity reminder to " + inactiveUsers.size() + " users at " + LocalDateTime.now());
    }
}
