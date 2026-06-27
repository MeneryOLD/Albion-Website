package com.albion.website.repository;

import com.albion.website.model.Picture;
import com.albion.website.model.PictureType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>, JpaSpecificationExecutor<Picture> {
    void deleteAllByEntityIdAndType(Long entityId, PictureType type);
    List<Picture> findAllByEntityIdAndType(Long entityId, PictureType type);
    @Modifying
    @Query("DELETE FROM Picture p WHERE p.entityId = :entityId AND p.type = :type AND p.id IN :ids")
    void deleteByEntityIdAndIds(Long entityId, PictureType type, List<Long> ids);
}
