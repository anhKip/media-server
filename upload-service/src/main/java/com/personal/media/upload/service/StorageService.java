package com.personal.media.upload.service;

import com.personal.media.upload.config.StorageProperties;
import com.personal.media.upload.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {

    private final Path rootLocation;

    @Autowired
    public StorageService(StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        init();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            // Generate a unique filename to avoid overriding
            String newFilename = UUID.randomUUID() + extension;
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(newFilename))
                    .normalize().toAbsolutePath();
                    
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check to prevent directory traversal attacks
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                return newFilename;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}
