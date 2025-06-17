package com.jobportal.jobportal_api.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobportal.jobportal_api.entity.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
