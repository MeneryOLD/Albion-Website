package com.albion.website.controller;

import com.albion.website.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ItemService itemService;
    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("products", itemService.getAllPublished());
        return "store";
    }

    @GetMapping("/store/{slug}")
    public String storePage(@PathVariable String slug, Model model) {
        return "item";
    }
}
