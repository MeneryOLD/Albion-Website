package com.albion.website.controller;

import com.albion.website.dto.ArticleRequestDto;
import com.albion.website.model.Article;
import com.albion.website.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminArticleRestController {
    private final ArticleService articleService;

    @GetMapping
    public Page<Article> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return articleService.getAllArticles(page, size);
    }

    @GetMapping("/{id}")
    public Article getById(@PathVariable Long id) {
        return articleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Article> create(
            @ModelAttribute ArticleRequestDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articleService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Article> update(
            @PathVariable Long id,
            @ModelAttribute ArticleRequestDto request) {

        Article updated = articleService.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}