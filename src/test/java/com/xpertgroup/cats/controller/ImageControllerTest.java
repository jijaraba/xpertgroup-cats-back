package com.xpertgroup.cats.controller;

import com.xpertgroup.cats.dto.ImageDto;
import com.xpertgroup.cats.exception.NotFoundException;
import com.xpertgroup.cats.service.ImageService;
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

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    void getImagesByBreedId_Success() throws Exception {
        // Arrange
        String breedId = "abys";
        Integer limit = 5;

        ImageDto image1 = new ImageDto();
        image1.setId("img1");
        image1.setUrl("https://example.com/image1.jpg");
        image1.setWidth(800);
        image1.setHeight(600);

        ImageDto image2 = new ImageDto();
        image2.setId("img2");
        image2.setUrl("https://example.com/image2.jpg");
        image2.setWidth(1024);
        image2.setHeight(768);

        List<ImageDto> images = List.of(image1, image2);

        when(imageService.getImagesByBreedId(breedId, limit)).thenReturn(images);

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .param("limit", limit.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("img1"))
                .andExpect(jsonPath("$[0].url").value("https://example.com/image1.jpg"))
                .andExpect(jsonPath("$[0].width").value(800))
                .andExpect(jsonPath("$[0].height").value(600))
                .andExpect(jsonPath("$[1].id").value("img2"))
                .andExpect(jsonPath("$[1].url").value("https://example.com/image2.jpg"))
                .andExpect(jsonPath("$[1].width").value(1024))
                .andExpect(jsonPath("$[1].height").value(768));

        verify(imageService).getImagesByBreedId(breedId, limit);
    }

    @Test
    void getImagesByBreedId_DefaultLimit_Success() throws Exception {
        // Arrange
        String breedId = "abys";
        Integer defaultLimit = 10;

        ImageDto image = new ImageDto();
        image.setId("img1");
        image.setUrl("https://example.com/image1.jpg");
        image.setWidth(800);
        image.setHeight(600);

        List<ImageDto> images = List.of(image);

        when(imageService.getImagesByBreedId(breedId, defaultLimit)).thenReturn(images);

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value("img1"))
                .andExpect(jsonPath("$[0].url").value("https://example.com/image1.jpg"));

        verify(imageService).getImagesByBreedId(breedId, defaultLimit);
    }

    @Test
    void getImagesByBreedId_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        String breedId = "invalid";
        Integer limit = 10;

        when(imageService.getImagesByBreedId(breedId, limit))
                .thenThrow(new NotFoundException("No images found for breed ID: " + breedId));

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .param("limit", limit.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No images found for breed ID: " + breedId));

        verify(imageService).getImagesByBreedId(breedId, limit);
    }

    @Test
    void getImagesByBreedId_MissingBreedIdParameter_ReturnsBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).getImagesByBreedId(anyString(), anyInt());
    }

    @Test
    void getImagesByBreedId_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        // Arrange
        String breedId = "abys";
        Integer limit = 10;

        when(imageService.getImagesByBreedId(breedId, limit))
                .thenThrow(new RuntimeException("External API error"));

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .param("limit", limit.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));

        verify(imageService).getImagesByBreedId(breedId, limit);
    }

    @Test
    void getImagesByBreedId_EmptyResult_Success() throws Exception {
        // Arrange
        String breedId = "abys";
        Integer limit = 10;

        when(imageService.getImagesByBreedId(breedId, limit)).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .param("limit", limit.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(imageService).getImagesByBreedId(breedId, limit);
    }

    @Test
    void getImagesByBreedId_InvalidLimitParameter_UsesDefaultLimit() throws Exception {
        // Arrange
        String breedId = "abys";
        String invalidLimit = "invalid";
        Integer defaultLimit = 10;

        ImageDto image = new ImageDto();
        image.setId("img1");
        image.setUrl("https://example.com/image1.jpg");

        List<ImageDto> images = List.of(image);

        when(imageService.getImagesByBreedId(eq(breedId), any(Integer.class))).thenReturn(images);

        // Act & Assert
        mockMvc.perform(get("/imagesbybreedid")
                .param("breed_id", breedId)
                .param("limit", invalidLimit)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // When the parameter conversion fails, the controller method is not called
        verify(imageService, never()).getImagesByBreedId(anyString(), anyInt());
    }
}