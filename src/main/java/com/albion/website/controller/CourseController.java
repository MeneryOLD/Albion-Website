package com.albion.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CourseController {

    @GetMapping("/language-courses/")
    public String getAllCourses() {
        return "course-list";
    }

}
