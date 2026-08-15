package com.albion.website.service;

import com.albion.website.Exception.NotFoundException;
import com.albion.website.dto.*;
import com.albion.website.model.Article;
import com.albion.website.model.PictureType;
import com.albion.website.repository.ArticleRepository;

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
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final SlugService slugService;
    private final PictureService pictureService;

    @Cacheable(value = "articles", key = "#slug")
    @Transactional(readOnly = true)
    public ArticleDto getBySlug(String slug) {
        Article article = articleRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new NotFoundException("Article not found"));

        return mapToDto(article);
    }

    public Page<Article> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return articleRepository.findAll(pageable);
    }

    @CacheEvict(value = "articles", allEntries = true)
    @Transactional
    public Article create(ArticleRequestDto request) {
        String slug;
        if (request.getSlug().isEmpty()) {
            slug = slugService.generateUniqueSlug(
                    request.getTitle(),
                    articleRepository::existsBySlug
            );
        } else {
            slug = request.getSlug();
        }

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(slug);
        article.setDescription(request.getDescription());
        article.setText(request.getText());
        article.setPublished(request.isPublished());

        Article saved = articleRepository.save(article);
        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                try {
                    pictureService.addPicture(
                            saved.getId(),
                            PictureType.ARTICLE,
                            file.getBytes()
                    );
                } catch (IOException e) {
                    throw new RuntimeException(("Failed to process image"));
                }
            }
        }
        return saved;
    }

    @CacheEvict(value = "articles", allEntries = true)
    @Transactional
    public Article update(Long id, ArticleRequestDto request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Article not found"));

        article.setTitle(request.getTitle());
        article.setDescription(request.getDescription());
        article.setText(request.getText());
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            article.setSlug(request.getSlug());
        }
        article.setPublished(request.isPublished());

        if (request.getPicturesToDelete() != null && !request.getPicturesToDelete().isEmpty()) {
            pictureService.deleteByEntityIdAndIds(id, PictureType.ARTICLE, request.getPicturesToDelete());
        }

        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                if (!file.isEmpty()) {
                    try {
                        pictureService.addPicture(id, PictureType.ARTICLE, file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process image: " + file.getOriginalFilename());
                    }
                }
            }
        }

        return articleRepository.save(article);
    }

    @Transactional
    public void delete(Long id) {
        try {
            pictureService.deleteAllPictures(id, PictureType.ARTICLE);
            articleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Article not found");
        }
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public void savePage(Long id, String html) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        article.setPageHtml(html);
        articleRepository.save(article);
    }

    public String getPageHtml(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        return article.getPageHtml();
    }

    @Transactional(readOnly = true)
    public List<ArticleCardDto> getPublishedArticles() {

        return articleRepository.findAllByPublishedTrue(
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
                .stream()
                .map(this::mapToCardDto)
                .toList();
    }

    private ArticleDto mapToDto(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getDescription(),
                article.getText(),
                article.getPageHtml(),
                article.getCreatedAt(),
                mapPictures(article.getId())
        );
    }

    private ArticleCardDto mapToCardDto(Article article) {
        return new ArticleCardDto(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getDescription(),
                article.getText(),
                article.getCreatedAt(),
                mapPictures(article.getId())
        );
    }

    private ArticlePreviewDto mapToPreviewDto(Article article) {
        return new ArticlePreviewDto(
                article.getTitle(),
                article.getSlug(),
                article.getDescription(),
                article.getCreatedAt()
        );
    }
    private List<PictureResponse> mapPictures(Long articleId) {
        return pictureService.getPictures(articleId, PictureType.ARTICLE)
                .stream()
                .map(p -> new PictureResponse(
                        p.getId(),
                        "/api/pictures/" + p.getId()
                ))
                .toList();
    }
}