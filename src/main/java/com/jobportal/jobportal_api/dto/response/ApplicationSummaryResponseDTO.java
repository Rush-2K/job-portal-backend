package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDate;

import com.jobportal.jobportal_api.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationSummaryResponseDTO {

    private Long applicationId;
    private Long jobId;
    private String applicantName;
    private String jobTitle;
    private ApplicationStatus applicationStatus;
    private LocalDate appliedDate;
}
