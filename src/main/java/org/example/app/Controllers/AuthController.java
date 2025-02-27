package org.example.app.Controllers;

import org.example.app.Exceptions.ExceptionUtil;
import org.example.app.Exceptions.UserException;
import org.example.app.Models.Dtos.RegistrationRequest;
import org.example.app.Models.Entities.Role;
import org.example.app.Models.Entities.User;
import org.example.app.RoleType;
import org.example.app.Security.JwtUtil;
import org.example.app.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) throws Exception{
        Role role = userService.findRole(RoleType.USER);

        try {
            userService.saveUser(new User(
                    registrationRequest.getFirstName(),
                    registrationRequest.getLastName(),
                    registrationRequest.getUsername(),
                    registrationRequest.getPassword(),
                    registrationRequest.getEmail(),
                    List.of(role)
                    )
            );
            return ResponseEntity.ok("Registration successfull");

        } catch (UserException e) {
            return ResponseEntity.status(400).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(400), e.getMessage(), "/register"));

        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) throws Exception {
//        throw new Exception("dada");

        if (loginRequest.get("password") == null || loginRequest.get("username") == null) {
            return ResponseEntity.status(400).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(400), "Missing username and password credentials", "/login"));
        }

        try {
            User user = userService.findUserByName(loginRequest.get("username"));
            if (user == null || !userService.verifyLogin(user, loginRequest.get("password"))) {
                logger.error("Incorrect user or password on login");
                return ResponseEntity.status(400).body(ExceptionUtil.buildErrorResponse(HttpStatus.valueOf(400), "Incorrect user or password", "/login"));
            }

            String jwt = jwtUtil.generateToken(
                    user.getUsername(),
                    user.getRoles().stream().map((role -> role.getRole())).collect(Collectors.toList())
            );

            Map<String, String> json = new HashMap<>();
            json.put("jwt", jwt);

            return ResponseEntity.ok(json);
        } catch (Exception e) {
            logger.error("error logging in: ", e);
            throw new Exception("An unexpected Error occurred.");
        }

    }
}
