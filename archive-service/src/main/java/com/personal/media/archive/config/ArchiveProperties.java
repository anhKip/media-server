package com.personal.media.archive.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("archive")
public class ArchiveProperties {
    private String archiveLocation;
    private String storageLocation;

    public String getArchiveLocation() {
        return archiveLocation;
    }

    public void setArchiveLocation(String archiveLocation) {
        this.archiveLocation = archiveLocation;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}
