package com.albion.website.dto;

import com.albion.website.model.CourseFormat;
import com.albion.website.model.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class CourseRequestDto {
    private String name;
    private String description;
    private String text;
    private BigDecimal price;
    private String duration;
    private String level;
    private String slug;
    private CourseFormat format;
    private CourseType type;
    private boolean published;
    private List<MultipartFile> pictures;
    private List<Long> picturesToDelete;
    private String language;
}