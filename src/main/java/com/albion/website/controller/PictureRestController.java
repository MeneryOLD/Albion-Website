package com.albion.website.controller;

import com.albion.website.dto.PictureResponse;
import com.albion.website.model.Picture;
import com.albion.website.model.PictureType;
import com.albion.website.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pictures")
public class PictureRestController {

    private final PictureService pictureService;

    @GetMapping("/entity/{entityId}")
    public ResponseEntity<List<PictureResponse>> getPicturesByEntity(
            @PathVariable Long entityId,
            @RequestParam PictureType type
    ) {

        List<Picture> pictures = pictureService.getPictures(entityId, type);

        List<PictureResponse> response = pictures.stream()
                .map(picture -> new PictureResponse(
                        picture.getId(),
                        "/api/pictures/" + picture.getId()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPictureContent(@PathVariable Long id) {
        Optional<Picture> optionalPicture = pictureService.getById(id);

        if (optionalPicture.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Picture picture = optionalPicture.get();

        byte[] imageBytes = picture.getPicture();

        String mimeType = new Tika().detect(imageBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CACHE_CONTROL, "max-age=31536000")
                .body(imageBytes);
    }
}