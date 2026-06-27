package com.albion.website.controller;

import com.albion.website.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final CourseService courseService;

    @GetMapping("/")
    public String homepage(Model model) {
        model.addAttribute("courses", courseService.getPopularCourses());
        return "home";
    }
}
