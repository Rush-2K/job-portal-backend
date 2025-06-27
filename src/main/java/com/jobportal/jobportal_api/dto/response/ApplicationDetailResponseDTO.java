package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDetailResponseDTO {

    private Long applicationId;
    private Long jobId;
    private String applicantName;
    private String applicantEmail;
    private String jobTitle;
    private String jobType;
    private String salary;
    private String resumeLink;
    private LocalDate appliedDate;
    private String applicationStatus;
}
