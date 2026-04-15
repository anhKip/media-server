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

@Service
public class ArchiveService {

    private final ArchiveProperties archiveProperties;
    private static final Logger log = LoggerFactory.getLogger(ArchiveService.class);

    public ArchiveService(ArchiveProperties archiveProperties) {
        this.archiveProperties = archiveProperties;
    }
    
    @RabbitListener(queues = RabbitMQConfig.PHOTO_QUEUE)
    public void processArchive(PhotoEventMsg msg) {
        log.info("Received archive request for file: {}", msg.getFileName());
        try {
            Path source = Paths.get(msg.getTempLocation());
            Path destination = Paths.get(archiveProperties.getArchiveLocation());
            Path file = source.resolve(msg.getFileName());

            if (!Files.exists(destination)) {
                Files.createDirectories(destination);
            }
            Files.move(source, file, StandardCopyOption.REPLACE_EXISTING);

            log.info("Archived file {} to {}", file, destination);
        } catch (IOException e) {
            log.error("Failed to archive file {}. Error: {}", msg.getFileName(), e.getMessage());
        }
    }
}
