package com.ShowAPIs.ShowAPI.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public record MoviePageResponse(List<MovieDto> movieDtos,
                                Integer pageNumber,
                                Integer pageSize,
                                Long totalElements,
                                Integer totalPages,
                                boolean isLast) {
}
