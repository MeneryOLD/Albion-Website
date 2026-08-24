package com.albion.website.dto;

import com.albion.website.model.CourseFormat;
import com.albion.website.model.CourseType;
import com.albion.website.model.Picture;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseDto {
    private String name;
    private String slug;
    private String description;
    private String text;
    private BigDecimal price;
    private String duration;
    private String level;
    private LocalDateTime createdAt;
    private List<Picture> pictures;
    private CourseFormat format;
    private CourseType type;
    private boolean published;
    private String language;
    private String pageHtml;

}
