package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewActiveJobDetailsResponseDTO {

    private String title;
    private String description;
    private String location;
    private String companyName;
    private Long salary;
    private String jobType;
    private LocalDateTime createdTime;
}
