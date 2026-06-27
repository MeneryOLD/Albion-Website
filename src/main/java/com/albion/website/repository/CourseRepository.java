package com.albion.website.repository;

import com.albion.website.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findBySlugAndPublishedTrue(String slug);
    Page<Course> findAllByPublishedTrue(Pageable pageable);
    boolean existsBySlug(String slug);
    List<Course> findByPublishedTrue(Sort sort);
}