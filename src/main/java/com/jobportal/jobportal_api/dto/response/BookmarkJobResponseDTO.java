package com.jobportal.jobportal_api.dto.response;

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
public class BookmarkJobResponseDTO {
    private Long bookmarkId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String message;
}
