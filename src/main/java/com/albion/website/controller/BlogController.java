package com.albion.website.controller;

import com.albion.website.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class BlogController {
    private final ArticleService articleService;

    @GetMapping("/blog")
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleService.getPublishedArticles());
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String getArticle(Model model, @ModelAttribute("slug") String slug) {
        model.addAttribute("article", articleService.getBySlug(slug));
        return "article";
    }

}