package com.jobportal.jobportal_api.dto.request;

import com.jobportal.jobportal_api.enums.ApplicationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateApplicationStatusRequestDTO {

    private Long applicationId;
    private Long jobId;
    private ApplicationStatus status;
}
