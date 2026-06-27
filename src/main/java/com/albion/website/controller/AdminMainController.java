package com.albion.website.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
@RequiredArgsConstructor
public class AdminMainController {

    @GetMapping
    public String getAdminPage() {
        return "admin";
    }

}
