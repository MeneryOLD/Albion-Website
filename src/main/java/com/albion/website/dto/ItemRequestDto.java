package com.albion.website.dto;

import com.albion.website.model.ItemCategory;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private String name;
    private String description;
    private String text;
    private String slug;
    private BigDecimal price;
    private Integer amount;
    private ItemCategory category;
    private boolean published;
    private List<MultipartFile> pictures;
    private List<Long> picturesToDelete;
}