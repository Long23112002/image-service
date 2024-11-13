package com.example.imageservice.repositories;

import com.example.imageservice.entities.FileUpload;
import com.example.imageservice.entities.enums.ProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends MongoRepository<FileUpload, Long> {

    List<FileUpload> findAllByProcess(Long process);

}
