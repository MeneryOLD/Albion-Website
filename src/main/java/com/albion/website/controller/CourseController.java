package com.albion.website.controller;

import com.albion.website.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/language-courses")
    public String getAllCourses(Model model) {
        model.addAttribute("courses", courseService.getPopularCourses());
        return "course-list";
    }

    @GetMapping("/language-courses/{slug}")
    public String coursePage(@PathVariable String slug, Model model) {
        return "course";
    }

}