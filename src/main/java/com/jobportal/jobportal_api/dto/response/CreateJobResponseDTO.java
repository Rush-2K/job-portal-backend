package com.jobportal.jobportal_api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateJobResponseDTO {

    private Long id;
    private String jobTitle;
    // name of the one who post it
    private String name;
}
