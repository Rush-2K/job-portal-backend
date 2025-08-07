package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAllActiveJobsResponseDTO {

    private Long id;
    private String title;
    private String location;
    private String companyName;
    private Long salary;
    private String jobType;
    private LocalDateTime createdTime;
}
