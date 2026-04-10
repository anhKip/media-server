package com.personal.media.upload.service;

import com.personal.media.common.dto.PhotoEventMsg;
import com.personal.media.upload.config.RabbitMQConfig;
import com.personal.media.upload.config.StorageProperties;
import com.personal.media.upload.exception.InvalidFileException;
import com.personal.media.upload.exception.StorageException;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.PostConstruct;

@Service
public class StorageService {

    private final Path rootLocation;
    private final List<String> allowedExtensions = new ArrayList<>();
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public StorageService(StorageProperties properties, RabbitTemplate rabbitTemplate) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.allowedExtensions.addAll(properties.getAllowedExtensions());
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage.", e);
        }
    }

    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new InvalidFileException("Failed to store empty file.");
            }
            String originalFilename = file.getOriginalFilename();

            // Check valid file name and extension
            String extension = "";
            if (originalFilename == null || !originalFilename.contains(".")) {
                throw new InvalidFileException("Invalid file name.");
            }
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if (!allowedExtensions.contains(extension)) {
                throw new InvalidFileException("Invalid file extension.");
            }
            
            // Generate a unique filename to avoid overriding
            String newFilename = UUID.randomUUID() + "." + extension;
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(newFilename))
                    .normalize().toAbsolutePath();
                    
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            // Prepare message
            PhotoEventMsg message = new PhotoEventMsg(newFilename, destinationFile.toString(), file.getSize());

            // Send message
            rabbitTemplate.convertAndSend(RabbitMQConfig.PHOTO_EXCHANGE, RabbitMQConfig.PHOTO_ROUTING_KEY, message);

            return newFilename;

        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }
}
