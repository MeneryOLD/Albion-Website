package com.albion.website.dto;

import com.albion.website.model.Picture;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String slug;
    private String description;
    private String text;
    private String pageHtml;
    private LocalDateTime createdAt;
    private List<PictureResponse> pictures;
}