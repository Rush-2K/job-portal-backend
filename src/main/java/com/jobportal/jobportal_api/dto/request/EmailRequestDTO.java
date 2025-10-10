package com.jobportal.jobportal_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDTO {

    @NotBlank
    private String recipientEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

}
