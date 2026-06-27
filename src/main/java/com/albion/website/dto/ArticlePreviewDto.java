package com.albion.website.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ArticlePreviewDto {
    private String title;
    private String slug;
    private String description;
    private LocalDateTime createdAt;
}