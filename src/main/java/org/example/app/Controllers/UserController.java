package org.example.app.Controllers;

import org.example.app.Exceptions.ExceptionUtil;
import org.example.app.Models.Entities.User;
import org.example.app.Models.Dtos.UserDto;
import org.example.app.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {
    private UserService userService;
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
         this.userService = userService;
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam String id) throws Exception {
        try {
            User user = userService.findUserById(UUID.fromString(id));
            if (user == null) {
                logger.error("user not found: {}", id);
                return ResponseEntity.status(404).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(404), "User not found", "/user"));
            }
            UserDto userDto = userService.toDto(user);
            return ResponseEntity.ok(userDto);
        } catch (Exception e) {
            throw new Exception("An unexpected error occurred");
        }
    }

    @GetMapping("/user/id")
    public ResponseEntity<?> getUserId(@RequestParam String username) throws Exception {
        try {
            User user = userService.findUserByName(username);
            if (user == null) {
                return ResponseEntity.status(404).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(404), "No user found with username " + username, "/user/id"));
            }
            return ResponseEntity.ok(Map.of("userId", user.getId()));
        } catch (Exception e) {
            throw new Exception("An unexpected error occurred");
        }
    }

}
