package org.example.app.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExceptionUtil {

    public static ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message, String path) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("status", String.valueOf(status.value()));
        errorResponse.put("path", path);
        errorResponse.put("time", String.valueOf(LocalDateTime.now()));
        return ResponseEntity.status(status).body(errorResponse);
    }

}
