package com.xpertgroup.cats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xpertgroup.cats.dto.BreedDto;
import com.xpertgroup.cats.exception.NotFoundException;
import com.xpertgroup.cats.service.CatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatController.class)
class CatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatService catService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBreeds_Success() throws Exception {
        // Arrange
        BreedDto breed1 = new BreedDto();
        breed1.setId("abys");
        breed1.setName("Abyssinian");
        breed1.setDescription("A breed of cat");

        BreedDto breed2 = new BreedDto();
        breed2.setId("aege");
        breed2.setName("Aegean");
        breed2.setDescription("Another breed of cat");

        List<BreedDto> breeds = List.of(breed1, breed2);

        when(catService.getAllBreeds()).thenReturn(breeds);

        // Act & Assert
        mockMvc.perform(get("/breeds")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("abys"))
                .andExpect(jsonPath("$[0].name").value("Abyssinian"))
                .andExpect(jsonPath("$[1].id").value("aege"))
                .andExpect(jsonPath("$[1].name").value("Aegean"));

        verify(catService).getAllBreeds();
    }

    @Test
    void getAllBreeds_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        // Arrange
        when(catService.getAllBreeds()).thenThrow(new RuntimeException("External API error"));

        // Act & Assert
        mockMvc.perform(get("/breeds")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));

        verify(catService).getAllBreeds();
    }

    @Test
    void getBreedById_Success() throws Exception {
        // Arrange
        String breedId = "abys";
        BreedDto breed = new BreedDto();
        breed.setId(breedId);
        breed.setName("Abyssinian");
        breed.setDescription("A breed of cat");
        breed.setOrigin("Egypt");

        when(catService.getBreedById(breedId)).thenReturn(breed);

        // Act & Assert
        mockMvc.perform(get("/breeds/{breedId}", breedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(breedId))
                .andExpect(jsonPath("$.name").value("Abyssinian"))
                .andExpect(jsonPath("$.description").value("A breed of cat"))
                .andExpect(jsonPath("$.origin").value("Egypt"));

        verify(catService).getBreedById(breedId);
    }

    @Test
    void getBreedById_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        String breedId = "invalid";

        when(catService.getBreedById(breedId)).thenThrow(new NotFoundException("Cat breed not found with ID: " + breedId));

        // Act & Assert
        mockMvc.perform(get("/breeds/{breedId}", breedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cat breed not found with ID: " + breedId));

        verify(catService).getBreedById(breedId);
    }

    @Test
    void searchBreeds_WithQuery_Success() throws Exception {
        // Arrange
        String query = "persian";
        BreedDto breed = new BreedDto();
        breed.setId("pers");
        breed.setName("Persian");
        breed.setDescription("A long-haired breed");

        List<BreedDto> breeds = List.of(breed);

        when(catService.searchBreeds(query)).thenReturn(breeds);

        // Act & Assert
        mockMvc.perform(get("/breeds/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("pers"))
                .andExpect(jsonPath("$[0].name").value("Persian"));

        verify(catService).searchBreeds(query);
    }

    @Test
    void searchBreeds_WithoutQuery_Success() throws Exception {
        // Arrange
        BreedDto breed1 = new BreedDto();
        breed1.setId("abys");
        breed1.setName("Abyssinian");

        BreedDto breed2 = new BreedDto();
        breed2.setId("aege");
        breed2.setName("Aegean");

        List<BreedDto> breeds = List.of(breed1, breed2);

        when(catService.searchBreeds(null)).thenReturn(breeds);

        // Act & Assert
        mockMvc.perform(get("/breeds/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("abys"))
                .andExpect(jsonPath("$[1].id").value("aege"));

        verify(catService).searchBreeds(null);
    }

    @Test
    void searchBreeds_EmptyResult_Success() throws Exception {
        // Arrange
        String query = "nonexistent";

        when(catService.searchBreeds(query)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/breeds/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(catService).searchBreeds(query);
    }
}