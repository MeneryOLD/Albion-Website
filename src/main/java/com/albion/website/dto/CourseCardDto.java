package com.albion.website.dto;

import java.math.BigDecimal;
import java.util.List;

public record CourseCardDto (
        Long id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        String level,
        String language,
        String type,
        String format,
        List<PictureResponse> pictures
) {}