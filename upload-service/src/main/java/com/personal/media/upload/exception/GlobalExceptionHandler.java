package com.personal.media.upload.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.personal.media.common.dto.UploadResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<UploadResponseDTO> handleInvalidFileException(InvalidFileException ex) {
        UploadResponseDTO response = new UploadResponseDTO("", "", ex.getMessage(), "FAILED", 0);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<UploadResponseDTO> handleStorageException(StorageException ex) {
        UploadResponseDTO response = new UploadResponseDTO("", "", ex.getMessage(), "ERROR", 0);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
