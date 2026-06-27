package com.albion.website.service;

import com.albion.website.model.Picture;
import com.albion.website.model.PictureType;
import com.albion.website.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureRepository pictureRepository;

    @Transactional(readOnly = true)
    public List<Picture> getPictures(Long entityId, PictureType type) {
        return pictureRepository.findAllByEntityIdAndType(entityId, type);
    }

    @Transactional
    public void addPicture(Long entityId, PictureType type, byte[] image) {
        Picture picture = new Picture();
        picture.setEntityId(entityId);
        picture.setType(type);
        picture.setPicture(image);
        pictureRepository.save(picture);
    }

    public Optional<Picture> getById(Long id) {
        return pictureRepository.findById(id);
    }

    @Transactional
    public void deleteAllPictures(Long entityId, PictureType type) {
        pictureRepository.deleteAllByEntityIdAndType(entityId, type);
    }

    @Transactional
    public void deleteByEntityIdAndIds(Long id, PictureType pictureType, List<Long> picturesToDelete) {
        pictureRepository.deleteByEntityIdAndIds(id, pictureType, picturesToDelete);
    }

}