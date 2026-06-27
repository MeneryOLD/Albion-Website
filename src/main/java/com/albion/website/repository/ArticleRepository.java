package com.albion.website.repository;

import com.albion.website.model.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlugAndPublishedTrue(String slug);
    boolean existsBySlug(String slug);
    List<Article> findAllByPublishedTrue(Sort sort);
}