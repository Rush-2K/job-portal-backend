package com.jobportal.jobportal_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jobportal.jobportal_api.dao.BookmarkRepository;
import com.jobportal.jobportal_api.dao.JobRepository;
import com.jobportal.jobportal_api.dao.UserRepository;
import com.jobportal.jobportal_api.dto.response.BookmarkJobResponseDTO;
import com.jobportal.jobportal_api.dto.response.RemoveBookmarkResponseDTO;
import com.jobportal.jobportal_api.dto.response.ViewBookmarkJobResponseDTO;
import com.jobportal.jobportal_api.entity.Bookmark;
import com.jobportal.jobportal_api.entity.Job;
import com.jobportal.jobportal_api.entity.User;
import com.jobportal.jobportal_api.mapper.BookmarkJobMapper;
import com.jobportal.jobportal_api.mapper.RemoveBookmarkMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookmarkService {

    private UserService userService;
    private JobRepository jobRepository;
    private BookmarkRepository bookmarkRepository;
    private UserRepository userRepository;
    private BookmarkJobMapper bookmarkJobMapper;
    private RemoveBookmarkMapper removeBookmarkMapper;

    public BookmarkService(UserService userService, JobRepository jobRepository, BookmarkRepository bookmarkRepository,
            UserRepository userRepository, BookmarkJobMapper bookmarkJobMapper,
            RemoveBookmarkMapper removeBookmarkMapper) {
        this.userService = userService;
        this.jobRepository = jobRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.userRepository = userRepository;
        this.bookmarkJobMapper = bookmarkJobMapper;
        this.removeBookmarkMapper = removeBookmarkMapper;
    }

    public BookmarkJobResponseDTO bookmarkJob(Long jobId) {
        // get user id from token
        Long userId = userService.getUserIdInsideToken();

        // get the user by userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not Found"));

        // get the job by jobId
        Job jobBookmarked = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not Found"));

        // check for duplication
        boolean checkDuplication = bookmarkRepository.existsByUser_IdAndJobs_Id(userId, jobId);

        if (checkDuplication) {
            throw new RuntimeException("Already bookmark this job");
        }

        // bookmark that job
        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setJobs(jobBookmarked);
        bookmarkRepository.save(bookmark);

        // return mapper
        BookmarkJobResponseDTO dto = bookmarkJobMapper.toBookmarkJobResponseDTO(bookmark, jobBookmarked);
        dto.setMessage("Bookmarked successfully!");

        return dto;

    }

    public RemoveBookmarkResponseDTO remove(Long jobId) {

        // get user id from token
        Long userId = userService.getUserIdInsideToken();

        // verify user id and job id with database
        Bookmark bookmarkToBeRemoved = bookmarkRepository.findByUser_IdAndJobs_Id(userId, jobId)
                .orElseThrow(() -> new NotFoundException("Bookmark not found"));

        // delete
        bookmarkRepository.delete(bookmarkToBeRemoved);

        // return
        RemoveBookmarkResponseDTO dto = removeBookmarkMapper.toRemoveBookmarkResponseDTO(bookmarkToBeRemoved);
        dto.setMessage("Bookmark removed successfully");

        return dto;

    }

    public List<ViewBookmarkJobResponseDTO> view() {
        // get userId inside token
        Long userId = userService.getUserIdInsideToken();

        List<ViewBookmarkJobResponseDTO> listOfBookmarkedJobs = bookmarkRepository.findBookmarksByUserId(userId);

        return listOfBookmarkedJobs;

    }

}
