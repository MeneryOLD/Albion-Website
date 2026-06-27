package com.albion.website.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CoursePreviewDto {
    private String name;
    private String slug;
    private String description;
    private LocalDateTime createdAt;
}