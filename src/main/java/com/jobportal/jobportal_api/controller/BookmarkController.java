package com.jobportal.jobportal_api.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.jobportal_api.dto.response.BookmarkJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.RemoveBookmarkResponseDTO;
import com.jobportal.jobportal_api.dtos.ApiResponseDto;
import com.jobportal.jobportal_api.enums.ApiStatus;
import com.jobportal.jobportal_api.service.BookmarkService;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    private BookmarkService bookmarkService;

    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<?> bookmarkJob(@PathVariable Long jobId) {
        BookmarkJobResponseDTO response = bookmarkService.bookmarkJob(jobId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                response.getMessage(), LocalDateTime.now(), response));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<?> removeBookmark(@PathVariable Long jobId) {
        RemoveBookmarkResponseDTO response = bookmarkService.remove(jobId);

        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponseDto<>(ApiStatus.SUCCESS, HttpStatus.OK.value(),
                response.getMessage(), LocalDateTime.now(), response));
    }

}
