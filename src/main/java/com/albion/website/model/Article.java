package com.albion.website.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean published = false;

    @Lob
    @Column(name = "page_html", columnDefinition = "LONGTEXT")
    private String pageHtml;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}