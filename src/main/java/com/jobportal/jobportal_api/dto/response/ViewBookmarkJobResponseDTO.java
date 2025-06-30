package com.jobportal.jobportal_api.dto.response;

import java.time.LocalDateTime;

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
public class ViewBookmarkJobResponseDTO {
    private Long bookmarkId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String location;
    private String jobType;
    private Long salary;
    private LocalDateTime bookmarkedAt;
}
