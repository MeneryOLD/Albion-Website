package com.albion.website.service;

import com.albion.website.Exception.NotFoundException;
import com.albion.website.dto.ItemRequestDto;
import com.albion.website.model.Item;
import com.albion.website.model.PictureType;
import com.albion.website.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final SlugService slugService;
    private final PictureService pictureService;

    @Cacheable(value = "items", key = "#slug")
    @Transactional(readOnly = true)
    public Item getBySlug(String slug) {
        return itemRepository
                .findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Transactional(readOnly = true)
    public List<Item> getAllPublished() {
        return itemRepository.findByPublishedTrueOrderByIdDesc();
    }

    public Page<Item> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        return itemRepository.findAll(pageable);
    }

    @CacheEvict(value = "items", allEntries = true)
    @Transactional
    public Item create(ItemRequestDto request) {
        String slug;
        if (request.getSlug().isEmpty()) {
            slug = slugService.generateUniqueSlug(
                    request.getName(),
                    itemRepository::existsBySlug
            );
        } else {
            slug = request.getSlug();
        }

        Item item = new Item();
        item.setName(request.getName());
        item.setSlug(slug);
        item.setDescription(request.getDescription());
        item.setText(request.getText());
        item.setPrice(request.getPrice());
        item.setAmount(request.getAmount());
        item.setCategory(request.getCategory());
        item.setPublished(request.isPublished());

        Item saved = itemRepository.save(item);
        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                try {
                    pictureService.addPicture(
                            saved.getId(),
                            PictureType.ITEM,
                            file.getBytes()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process image");
                }
            }
        }

        return saved;
    }

    @CacheEvict(value = "items", allEntries = true)
    @Transactional
    public Item update(Long id, ItemRequestDto request) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setText(request.getText());
        item.setSlug(request.getSlug());
        item.setPrice(request.getPrice());
        item.setAmount(request.getAmount());
        item.setCategory(request.getCategory());
        item.setPublished(request.isPublished());

        if (request.getPicturesToDelete() != null && !request.getPicturesToDelete().isEmpty()) {
            pictureService.deleteByEntityIdAndIds(id, PictureType.ITEM, request.getPicturesToDelete());
        }

        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                if (!file.isEmpty()) {
                    try {
                        pictureService.addPicture(id, PictureType.ITEM, file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process image: " + file.getOriginalFilename());
                    }
                }
            }
        }

        return itemRepository.save(item);
    }

    public void savePage(Long id, String html) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        item.setPageHtml(html);
        itemRepository.save(item);
    }

    public String getPageHtml(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        return item.getPageHtml();
    }

    @Transactional
    public void delete(Long id) {
        try {
            pictureService.deleteAllPictures(id, PictureType.ITEM);
            itemRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("Item not found");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }
}