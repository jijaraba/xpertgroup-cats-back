package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.LoginRequestDto;
import com.xpertgroup.cats.dto.RegistrationRequestDto;
import com.xpertgroup.cats.dto.UserDto;
import com.xpertgroup.cats.entity.User;
import com.xpertgroup.cats.exception.InvalidCredentialsException;
import com.xpertgroup.cats.exception.UserExistsException;
import com.xpertgroup.cats.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void login_Success() {
        // Arrange
        String username = "testuser";
        String password = "password123";
        String encodedPassword = passwordEncoder.encode(password);

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDto result = userService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Test User", result.getFullName());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void login_UserNotFound_ThrowsInvalidCredentialsException() {
        // Arrange
        String username = "nonexistent";
        String password = "password123";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void login_InvalidPassword_ThrowsInvalidCredentialsException() {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";
        String correctPassword = "password123";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(InvalidCredentialsException.class,
                () -> userService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());

        verify(userRepository).findByUsername(username);
    }

    @Test
    void register_Success() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(username);
        savedUser.setEmail(email);
        savedUser.setFullName(fullName);
        savedUser.setPassword(passwordEncoder.encode(password));
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.register(registrationRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals(fullName, result.getFullName());

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_UsernameExists_ThrowsUserExistsException() {
        // Arrange
        String username = "existinguser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);

        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        UserExistsException exception = assertThrows(UserExistsException.class,
                () -> userService.register(registrationRequest));
        assertEquals("Username already exists: " + username, exception.getMessage());

        verify(userRepository).existsByUsername(username);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_EmailExists_ThrowsUserExistsException() {
        // Arrange
        String username = "newuser";
        String email = "existing@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        UserExistsException exception = assertThrows(UserExistsException.class,
                () -> userService.register(registrationRequest));
        assertEquals("Email already exists: " + email, exception.getMessage());

        verify(userRepository).existsByUsername(username);
        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_PasswordIsEncoded() {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            user.setCreatedAt(LocalDateTime.now());
            return user;
        });

        // Act
        UserDto result = userService.register(registrationRequest);

        // Assert
        assertNotNull(result);

        // Verify that the save method was called with a user whose password is encoded
        verify(userRepository).save(argThat(user -> {
            // The password should be encoded and different from the original
            return !password.equals(user.getPassword()) && 
                   passwordEncoder.matches(password, user.getPassword());
        }));
    }
}