package com.xpertgroup.cats.controller;

import com.xpertgroup.cats.dto.LoginRequestDto;
import com.xpertgroup.cats.dto.RegistrationRequestDto;
import com.xpertgroup.cats.dto.UserDto;
import com.xpertgroup.cats.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user operations
 * Note: Using GET methods as per requirements, though POST would be more appropriate for login/register
 */
@RestController
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * User login endpoint
     * Note: Using GET as per requirements, though POST would be more secure and appropriate
     * @param username the username
     * @param password the password
     * @return User information if login is successful
     */
    @GetMapping("/login")
    public ResponseEntity<UserDto> login(
            @RequestParam String username,
            @RequestParam String password) {
        
        logger.info("GET /login - Login attempt for username: {}", username);
        
        LoginRequestDto loginRequest = new LoginRequestDto(username, password);
        UserDto user = userService.login(loginRequest);
        
        logger.info("GET /login - User successfully logged in: {}", username);
        return ResponseEntity.ok(user);
    }
    
    /**
     * User registration endpoint
     * Note: Using GET as per requirements, though POST would be more appropriate
     * @param username the username
     * @param email the email
     * @param fullName the full name
     * @param password the password
     * @return User information if registration is successful
     */
    @GetMapping("/register")
    public ResponseEntity<UserDto> register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String fullName,
            @RequestParam String password) {
        
        logger.info("GET /register - Registration attempt for username: {}", username);
        
        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);
        UserDto user = userService.register(registrationRequest);
        
        logger.info("GET /register - User successfully registered: {}", username);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    /**
     * Alternative login endpoint using POST (more secure and RESTful)
     * @param loginRequest the login request body
     * @return User information if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> loginPost(@Valid @RequestBody LoginRequestDto loginRequest) {
        logger.info("POST /login - Login attempt for username: {}", loginRequest.getUsername());
        
        UserDto user = userService.login(loginRequest);
        
        logger.info("POST /login - User successfully logged in: {}", loginRequest.getUsername());
        return ResponseEntity.ok(user);
    }
    
    /**
     * Alternative registration endpoint using POST (more secure and RESTful)
     * @param registrationRequest the registration request body
     * @return User information if registration is successful
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerPost(@Valid @RequestBody RegistrationRequestDto registrationRequest) {
        logger.info("POST /register - Registration attempt for username: {}", registrationRequest.getUsername());
        
        UserDto user = userService.register(registrationRequest);
        
        logger.info("POST /register - User successfully registered: {}", registrationRequest.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}