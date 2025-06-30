package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.BookmarkJobResponseDTO;
import com.jobportal.jobportal_api.entity.Bookmark;
import com.jobportal.jobportal_api.entity.Job;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookmarkJobMapper {

    @Mapping(target = "bookmarkId", source = "bookmark.id")
    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "jobTitle", source = "job.title")
    @Mapping(target = "companyName", source = "job.companyName")
    BookmarkJobResponseDTO toBookmarkJobResponseDTO(Bookmark bookmark, Job job);
}
