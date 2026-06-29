package com.albion.website.controller;

import com.albion.website.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/course-page")
public class CoursePageController {
    private final CourseService courseService;

    // POST — сохранить HTML из GrapesJS (только ADMIN)
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> savePage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        courseService.savePage(id, body.get("html"));
        return ResponseEntity.ok().build();
    }

    // GET — загрузить HTML для редактора
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getPage(@PathVariable Long id) {
        String html = courseService.getPageHtml(id);
        return ResponseEntity.ok(Map.of("html", html != null ? html : ""));
    }
}