package com.albion.website.controller;

import com.albion.website.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/article-page")
public class AdminArticlePageController {
    private final ArticleService articleService;

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> savePage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        articleService.savePage(id, body.get("html"));
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getPage(@PathVariable Long id) {
        String html = articleService.getPageHtml(id);
        return ResponseEntity.ok(Map.of("html", html != null ? html : ""));
    }
}
