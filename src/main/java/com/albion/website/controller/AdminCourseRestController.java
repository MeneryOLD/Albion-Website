package com.albion.website.controller;

import com.albion.website.dto.CourseRequestDto;
import com.albion.website.model.Course;
import com.albion.website.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/courses")
@RequiredArgsConstructor
public class AdminCourseRestController {
    private final CourseService courseService;

    @GetMapping
    public Page<Course> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return courseService.getAllCourses(page, size);
    }

    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return courseService.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Course> create(
            @ModelAttribute CourseRequestDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Course update(
            @PathVariable Long id,
            @ModelAttribute CourseRequestDto request
    ) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}