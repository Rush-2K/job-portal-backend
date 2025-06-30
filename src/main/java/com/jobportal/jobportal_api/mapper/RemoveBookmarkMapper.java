package com.jobportal.jobportal_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.jobportal.jobportal_api.dto.response.RemoveBookmarkResponseDTO;
import com.jobportal.jobportal_api.entity.Bookmark;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RemoveBookmarkMapper {

    @Mapping(target = "bookmarkId", source = "id")
    RemoveBookmarkResponseDTO toRemoveBookmarkResponseDTO(Bookmark bookmark);
}
