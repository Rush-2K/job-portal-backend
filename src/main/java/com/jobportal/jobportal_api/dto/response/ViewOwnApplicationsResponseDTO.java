package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ViewOwnApplicationsResponseDTO {

    private String jobTitle;
    private String companyName;
    private String applicationStatus;
    private LocalDate dateApplied;
    private String resumeLink;
    private String jobLocation;
}
