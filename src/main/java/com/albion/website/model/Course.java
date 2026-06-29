package com.albion.website.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(nullable = false)
    private BigDecimal price;

    private String duration;
    private String level;
    private boolean published = false;
    private LocalDateTime createdAt;
    private String language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseFormat format;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseType type;

    @Lob
    @Column(name = "page_html", columnDefinition = "LONGTEXT")
    private String pageHtml;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}