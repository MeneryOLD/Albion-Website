package com.albion.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller()
public class BlogController {
    @GetMapping("/blog/")
    public String getAllPosts() {
        return "blog-list";
    }
}
