package org.example.app.Exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.example.app.Controllers.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        logger.error("upload error: ", e);
        return ResponseEntity.status(413).body(ExceptionUtil.buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeds the maximum limit!", request.getServletPath()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMaxSizeException(MissingServletRequestParameterException e, HttpServletRequest request) {
        logger.error("Missing parameters: ", e);
        String errorMessage = "Missing Parameters: " + e.getParameterName();
        return ResponseEntity.status(413).body(ExceptionUtil.buildErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, errorMessage , request.getServletPath()));
    }
}
