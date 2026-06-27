package com.albion.website.controller;

import com.albion.website.repository.ItemRepository;
import com.albion.website.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StoreController {
    private final ItemService itemService;
    @GetMapping("/store")
    public String store(Model model) {
        model.addAttribute("products", itemService.getAllPublished());
        return "store";
    }
}
