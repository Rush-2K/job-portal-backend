package com.jobportal.jobportal_api.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateJobDetailsRequestDTO {

    private String title;
    private String description;
    private String location;
    private String companyName;
    private Long salary;
    private String jobType;
}
