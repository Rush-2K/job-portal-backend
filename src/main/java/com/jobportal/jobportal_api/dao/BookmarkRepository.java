package com.jobportal.jobportal_api.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jobportal.jobportal_api.dto.response.ViewBookmarkJobResponseDTO;
import com.jobportal.jobportal_api.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUser_IdAndJobs_Id(Long userId, Long jobId);

    Optional<Bookmark> findByUser_IdAndJobs_Id(Long userId, Long jobId);

    @Query("SELECT new com.jobportal.jobportal_api.dto.response.ViewBookmarkJobResponseDTO(" +
            "b.id, j.id, j.title, j.companyName, j.location, j.jobType, j.salary, b.createdAt) " +
            "FROM Bookmark b " +
            "JOIN b.jobs j " +
            "WHERE b.user.id = :userId")
    List<ViewBookmarkJobResponseDTO> findBookmarksByUserId(Long userId);

}
