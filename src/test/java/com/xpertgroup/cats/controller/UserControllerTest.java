package com.xpertgroup.cats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpertgroup.cats.dto.LoginRequestDto;
import com.xpertgroup.cats.dto.RegistrationRequestDto;
import com.xpertgroup.cats.dto.UserDto;
import com.xpertgroup.cats.exception.InvalidCredentialsException;
import com.xpertgroup.cats.exception.UserExistsException;
import com.xpertgroup.cats.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_GET_Success() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "password123";

        UserDto userDto = new UserDto(1L, username, "test@example.com", "Test User");

        when(userService.login(any(LoginRequestDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/login")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));

        verify(userService).login(any(LoginRequestDto.class));
    }

    @Test
    void login_GET_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";

        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(get("/login")
                .param("username", username)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Invalid Credentials"))
                .andExpect(jsonPath("$.message").value("Invalid username or password"));

        verify(userService).login(any(LoginRequestDto.class));
    }

    @Test
    void register_GET_Success() throws Exception {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        UserDto userDto = new UserDto(1L, username, email, fullName);

        when(userService.register(any(RegistrationRequestDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/register")
                .param("username", username)
                .param("email", email)
                .param("fullName", fullName)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fullName").value(fullName));

        verify(userService).register(any(RegistrationRequestDto.class));
    }

    @Test
    void register_GET_UserExists_ReturnsConflict() throws Exception {
        // Arrange
        String username = "existinguser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        when(userService.register(any(RegistrationRequestDto.class)))
                .thenThrow(new UserExistsException("Username already exists: " + username));

        // Act & Assert
        mockMvc.perform(get("/register")
                .param("username", username)
                .param("email", email)
                .param("fullName", fullName)
                .param("password", password)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("User Already Exists"))
                .andExpect(jsonPath("$.message").value("Username already exists: " + username));

        verify(userService).register(any(RegistrationRequestDto.class));
    }

    @Test
    void login_POST_Success() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "password123";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);
        UserDto userDto = new UserDto(1L, username, "test@example.com", "Test User");

        when(userService.login(any(LoginRequestDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.fullName").value("Test User"));

        verify(userService).login(any(LoginRequestDto.class));
    }

    @Test
    void login_POST_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        String username = "testuser";
        String password = "wrongpassword";

        LoginRequestDto loginRequest = new LoginRequestDto(username, password);

        when(userService.login(any(LoginRequestDto.class)))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Invalid Credentials"));

        verify(userService).login(any(LoginRequestDto.class));
    }

    @Test
    void register_POST_Success() throws Exception {
        // Arrange
        String username = "newuser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);
        UserDto userDto = new UserDto(1L, username, email, fullName);

        when(userService.register(any(RegistrationRequestDto.class))).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.fullName").value(fullName));

        verify(userService).register(any(RegistrationRequestDto.class));
    }

    @Test
    void register_POST_UserExists_ReturnsConflict() throws Exception {
        // Arrange
        String username = "existinguser";
        String email = "newuser@example.com";
        String fullName = "New User";
        String password = "password123";

        RegistrationRequestDto registrationRequest = new RegistrationRequestDto(username, email, fullName, password);

        when(userService.register(any(RegistrationRequestDto.class)))
                .thenThrow(new UserExistsException("Username already exists: " + username));

        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("User Already Exists"));

        verify(userService).register(any(RegistrationRequestDto.class));
    }

    @Test
    void login_POST_InvalidRequestBody_ReturnsBadRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"username\":\"\",\"password\":\"\"}";

        // Act & Assert
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));

        verify(userService, never()).login(any(LoginRequestDto.class));
    }

    @Test
    void register_POST_InvalidRequestBody_ReturnsBadRequest() throws Exception {
        // Arrange
        String invalidJson = "{\"username\":\"\",\"email\":\"invalid-email\",\"fullName\":\"\",\"password\":\"\"}";

        // Act & Assert
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.validationErrors").exists());

        verify(userService, never()).register(any(RegistrationRequestDto.class));
    }
}