package com.personal.media.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.personal.media.archive.config.ArchiveProperties;

@SpringBootApplication
@EnableConfigurationProperties(ArchiveProperties.class)
public class ArchiveServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchiveServiceApplication.class, args);
    }
}
