package com.example.imageservice.repositories;

import com.example.imageservice.dtos.filter.ImageParam;
import com.example.imageservice.entities.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query(
          "SELECT i FROM Image i WHERE (:#{#imageParam.objectId} IS NULL OR i.objectId = :#{#imageParam.objectId}) "
                  + "AND (:#{#imageParam.objectName} IS NULL OR i.objectName = :#{#imageParam.objectName})")
  Page<Image> filter( ImageParam imageParam, Pageable pageable);

}
