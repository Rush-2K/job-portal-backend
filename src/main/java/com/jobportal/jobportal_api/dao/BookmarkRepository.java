package com.jobportal.jobportal_api.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.jobportal_api.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUser_IdAndJobs_Id(Long userId, Long jobId);

    Optional<Bookmark> findByUser_IdAndJobs_Id(Long userId, Long jobId);
}
