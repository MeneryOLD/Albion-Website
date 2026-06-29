package com.albion.website.service;

import com.albion.website.Exception.NotFoundException;
import com.albion.website.dto.*;
import com.albion.website.model.Course;
import com.albion.website.model.Picture;
import com.albion.website.model.PictureType;
import com.albion.website.repository.CourseRepository;
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
public class CourseService {
    private final CourseRepository courseRepository;
    private final SlugService slugService;
    private final PictureService pictureService;

    @Cacheable(value = "courses", key = "#slug")
    @Transactional(readOnly = true)
    public Course getBySlug(String slug) {
        Course course = courseRepository
                .findBySlugAndPublishedTrue(slug)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        return course;
    }

    @Transactional(readOnly = true)
    public Page<CoursePreviewDto> getPublished(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return courseRepository
                .findAllByPublishedTrue(pageable)
                .map(this::mapToPreviewDto);
    }

    @Transactional(readOnly = true)
    public List<CourseCardDto> getPopularCourses() {

        return courseRepository
                .findByPublishedTrue(
                        Sort.by(Sort.Direction.DESC, "createdAt")
                )
                .stream()
                .map(course -> {

                    List<PictureResponse> pictures = pictureService
                            .getPictures(course.getId(), PictureType.COURSE)
                            .stream()
                            .map(picture -> new PictureResponse(
                                    picture.getId(),
                                    "/api/pictures/" + picture.getId()
                            ))
                            .toList();

                    return new CourseCardDto(
                            course.getId(),
                            course.getTitle(),
                            course.getSlug(),
                            course.getDescription(),
                            course.getPrice(),
                            course.getLevel(),
                            course.getLanguage(),
                            course.getType().name(),
                            course.getFormat().name(),
                            pictures
                    );
                })
                .toList();
    }

    public Page<Course> getAllCourses(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return courseRepository.findAll(pageable);
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Transactional
    public Course create(CourseRequestDto request) {
        String slug;
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            slug = slugService.generateUniqueSlug(
                    request.getName(),
                    courseRepository::existsBySlug
            );
        } else {
            slug = request.getSlug().trim();
        }

        Course course = new Course();
        course.setTitle(request.getName());
        course.setSlug(slug);
        course.setDescription(request.getDescription());
        course.setText(request.getText());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setLanguage(request.getLanguage());
        course.setLevel(request.getLevel());
        course.setFormat(request.getFormat());
        course.setType(request.getType());
        course.setPublished(request.isPublished());

        Course saved = courseRepository.save(course);

        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                try {
                    pictureService.addPicture(
                            saved.getId(),
                            PictureType.COURSE,
                            file.getBytes()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process image");
                }
            }
        }

        return saved;
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Transactional
    public Course update(Long id, CourseRequestDto request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.setTitle(request.getName());
        course.setDescription(request.getDescription());
        course.setText(request.getText());
        course.setPrice(request.getPrice());
        course.setSlug(request.getSlug());
        course.setDuration(request.getDuration());
        course.setLevel(request.getLevel());
        course.setLevel(request.getLevel());
        course.setFormat(request.getFormat());
        course.setType(request.getType());
        course.setLanguage(request.getLanguage());
        course.setPublished(request.isPublished());

        if (request.getPicturesToDelete() != null && !request.getPicturesToDelete().isEmpty()) {
            pictureService.deleteByEntityIdAndIds(id, PictureType.COURSE, request.getPicturesToDelete());
        }

        if (request.getPictures() != null) {
            for (MultipartFile file : request.getPictures()) {
                if (!file.isEmpty()) {
                    try {
                        pictureService.addPicture(id, PictureType.COURSE, file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process image: " + file.getOriginalFilename());
                    }
                }
            }
        }

        return courseRepository.save(course);
    }

    @Transactional
    public void delete(Long id) {
        try {
            pictureService.deleteAllPictures(id, PictureType.COURSE);
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Course not found");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    private CourseDto mapToDto(Course course) {
        List<Picture> pictures = pictureService.getPictures(
                course.getId(),
                PictureType.COURSE
        );

        return new CourseDto(
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getText(),
                course.getPrice(),
                course.getDuration(),
                course.getLevel(),
                course.getCreatedAt(),
                pictures,
                course.getFormat(),
                course.getType(),
                course.isPublished(),
                course.getLanguage()
                );
    }

    public void savePage(Long id, String html) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        course.setPageHtml(html);
        courseRepository.save(course);
    }

    public String getPageHtml(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found: " + id));
        return course.getPageHtml();
    }

    private CoursePreviewDto mapToPreviewDto(Course course) {
        return new CoursePreviewDto(
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getCreatedAt()
        );
    }
}