package com.personal.media.upload.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.personal.media.common.dto.UploadResponseDTO;
import com.personal.media.upload.service.StorageService;

@RestController
@RequestMapping("/api/v1/upload")
public class UploadController {
    private final StorageService storageService;

    public UploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("")
    public ResponseEntity<UploadResponseDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        String filePath = storageService.store(file);   

        UploadResponseDTO response = new UploadResponseDTO(filePath, file.getOriginalFilename(), "File uploaded successfully", "SUCCESS", file.getSize());


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
