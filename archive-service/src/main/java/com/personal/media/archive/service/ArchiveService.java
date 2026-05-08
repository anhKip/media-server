package com.personal.media.archive.service;

import org.springframework.stereotype.Service;
import com.personal.media.archive.config.ArchiveProperties;
import com.personal.media.common.dto.PhotoEventMsg;
import com.personal.media.archive.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

@Service
public class ArchiveService {

    private final String archiveLocation;
    private static final Logger log = LoggerFactory.getLogger(ArchiveService.class);

    @Autowired
    public ArchiveService(ArchiveProperties archiveProperties) {
        this.archiveLocation = archiveProperties.getLocation();    }

    @PostConstruct
    public void init() {
        // Defensive check: don't let it reach Paths.get() if it's null
        if (this.archiveLocation == null) {
            log.error("❌ CRITICAL: archive.location is NULL. Check your application.yml and .env mapping.");
            throw new IllegalStateException("Archive storage path is not configured!");
        }

        try {
            Path path = Paths.get(this.archiveLocation);
            log.info("🚀 Initializing storage at: {}", path.toAbsolutePath());
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
        // try {
        //     Files.createDirectories(Paths.get(archiveLocation));
        // } catch (IOException e) {
        //     throw new RuntimeException("Failed to initialize storage", e);
        // }
    }
    
    @RabbitListener(queues = RabbitMQConfig.PHOTO_QUEUE)
    public void processArchive(PhotoEventMsg msg) {
        log.info("Received archive request for file: {}", msg.getFileId());
        try {
            Path source = Paths.get(msg.getTempLocation());
            Path destination = Paths.get(archiveLocation);
            Path file = source.resolve(msg.getFileId());

            if (!Files.exists(destination)) {
                Files.createDirectories(destination);
            }
            Files.move(source, file, StandardCopyOption.REPLACE_EXISTING);

            log.info("Archived file {} to {}", file, destination);
        } catch (IOException e) {
            log.error("Failed to archive file {}. Error: {}", msg.getFileId(), e.getMessage());
        }
    }
}
