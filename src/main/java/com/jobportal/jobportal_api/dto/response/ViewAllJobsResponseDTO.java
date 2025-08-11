package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ViewAllJobsResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String companyName;
    private Boolean jobStatus;
    private Long salary;
    private String jobType;
    private Long applicationCount;
    private LocalDateTime createdTime;

}
