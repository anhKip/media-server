package com.personal.media.common.dto;

public class UploadResponseDTO {

    private String fileId;
    private String fileName;
    private String message;
    private String status;
    private long size;

    public UploadResponseDTO(String fileId, String fileName, String message, String status, long size) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.message = message;
        this.status = status;
        this.size = size;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
