package com.jobportal.jobportal_api.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateJobStatusRequestDTO {
    private Boolean jobStatus;
}
