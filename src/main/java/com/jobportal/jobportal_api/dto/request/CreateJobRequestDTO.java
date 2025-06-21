package com.jobportal.jobportal_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateJobRequestDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String location;
    @NotBlank
    private String companyName;
    @NotBlank
    private Long salary;
    @NotBlank
    private String jobType;
}
