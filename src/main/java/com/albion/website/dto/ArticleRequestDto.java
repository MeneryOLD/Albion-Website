package com.albion.website.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ArticleRequestDto {
    private String title;
    private String description;
    private String text;
    private String slug;
    private boolean published;
    private List<MultipartFile> pictures;
    private List<Long> picturesToDelete;
}