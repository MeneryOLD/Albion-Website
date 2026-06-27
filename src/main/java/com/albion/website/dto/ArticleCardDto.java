package com.albion.website.dto;

import java.time.LocalDateTime;
import java.util.List;


public record ArticleCardDto (
    Long id,
    String title,
    String slug,
    String description,
    String text,
    LocalDateTime createdAt,
    List<PictureResponse> pictures
    ) {}