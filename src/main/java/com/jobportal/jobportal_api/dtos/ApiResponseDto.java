package com.jobportal.jobportal_api.dtos;

import java.time.LocalDateTime;

import com.jobportal.jobportal_api.enums.ApiStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDto<T> {

    private ApiStatus status;
    private int code;
    private String message;
    private LocalDateTime timeStamp;
    private T data;
}
