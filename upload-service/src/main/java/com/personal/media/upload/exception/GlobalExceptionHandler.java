package com.personal.media.upload.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.personal.media.common.dto.UploadResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<UploadResponseDTO> handleStorageException(MaxUploadSizeExceededException ex) {
        UploadResponseDTO response = new UploadResponseDTO("", "", "File size exceeds the maximum allowed size (10MB).", "FAILED", 0);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<UploadResponseDTO> handleStorageException(StorageException ex) {
        UploadResponseDTO response = new UploadResponseDTO("", "", ex.getMessage(), "FAILED", 0);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
