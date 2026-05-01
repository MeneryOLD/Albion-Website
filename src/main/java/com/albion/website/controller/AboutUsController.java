package com.albion.website.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("")
public class AboutUsController {
    @GetMapping("/about-us/")
    public String getAboutUsPage() {
        return "about-us";
    }
}
