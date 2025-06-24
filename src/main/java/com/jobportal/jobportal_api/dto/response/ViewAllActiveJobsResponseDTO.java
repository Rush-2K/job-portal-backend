package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewAllActiveJobsResponseDTO {

    private String title;
    private String location;
    private LocalDateTime createdTime;
}
