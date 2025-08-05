package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.LoginRequestDto;
import com.xpertgroup.cats.dto.RegistrationRequestDto;
import com.xpertgroup.cats.dto.UserDto;
import com.xpertgroup.cats.entity.User;
import com.xpertgroup.cats.exception.InvalidCredentialsException;
import com.xpertgroup.cats.exception.UserExistsException;
import com.xpertgroup.cats.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for user operations
 */
@Service
@Transactional
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /**
     * Authenticate user with username and password
     * @param loginRequest the login credentials
     * @return UserDto if authentication is successful
     * @throws InvalidCredentialsException if credentials are invalid
     */
    public UserDto login(LoginRequestDto loginRequest) {
        logger.info("Attempting login for username: {}", loginRequest.getUsername());
        
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    logger.warn("Login attempt failed - user not found: {}", loginRequest.getUsername());
                    return new InvalidCredentialsException("Invalid username or password");
                });
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Login attempt failed - invalid password for user: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }
        
        logger.info("User successfully logged in: {}", loginRequest.getUsername());
        return convertToDto(user);
    }
    
    /**
     * Register a new user
     * @param registrationRequest the registration data
     * @return UserDto of the newly created user
     * @throws UserExistsException if username or email already exists
     */
    public UserDto register(RegistrationRequestDto registrationRequest) {
        logger.info("Attempting to register new user: {}", registrationRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", registrationRequest.getUsername());
            throw new UserExistsException("Username already exists: " + registrationRequest.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", registrationRequest.getEmail());
            throw new UserExistsException("Email already exists: " + registrationRequest.getEmail());
        }
        
        // Create new user
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setFullName(registrationRequest.getFullName());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        
        User savedUser = userRepository.save(user);
        logger.info("User successfully registered: {}", savedUser.getUsername());
        
        return convertToDto(savedUser);
    }
    
    /**
     * Convert User entity to UserDto
     * @param user the user entity
     * @return UserDto
     */
    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName()
        );
    }
}