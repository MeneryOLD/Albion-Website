package com.albion.website.controller;

import com.albion.website.dto.ItemRequestDto;
import com.albion.website.model.Item;
import com.albion.website.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/items")
@RequiredArgsConstructor
public class AdminItemRestController {
    private final ItemService itemService;

    @GetMapping
    public Page<Item> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return itemService.getAllItems(page, size);
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) {
        return itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Item> create(
            @ModelAttribute ItemRequestDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemService.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Item update(
            @PathVariable Long id,
            @ModelAttribute ItemRequestDto request
    ) {
        return itemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
