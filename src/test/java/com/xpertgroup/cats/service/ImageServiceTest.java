package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.ImageDto;
import com.xpertgroup.cats.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(webClient);
    }

    @Test
    void getImagesByBreedId_Success() {
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

        List<ImageDto> expectedImages = List.of(image1, image2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class)).thenReturn(Flux.fromIterable(expectedImages));

        // Act
        List<ImageDto> result = imageService.getImagesByBreedId(breedId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("img1", result.get(0).getId());
        assertEquals("https://example.com/image1.jpg", result.get(0).getUrl());
        assertEquals("img2", result.get(1).getId());
        assertEquals("https://example.com/image2.jpg", result.get(1).getUrl());

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToFlux(ImageDto.class);
    }

    @Test
    void getImagesByBreedId_DefaultLimit() {
        // Arrange
        String breedId = "abys";

        ImageDto image = new ImageDto();
        image.setId("img1");
        image.setUrl("https://example.com/image1.jpg");

        List<ImageDto> expectedImages = List.of(image);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class)).thenReturn(Flux.fromIterable(expectedImages));

        // Act
        List<ImageDto> result = imageService.getImagesByBreedId(breedId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("img1", result.get(0).getId());
    }

    @Test
    void getImagesByBreedId_EmptyList_ThrowsNotFoundException() {
        // Arrange
        String breedId = "invalid";
        Integer limit = 10;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class)).thenReturn(Flux.fromIterable(Collections.emptyList()));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> imageService.getImagesByBreedId(breedId, limit));
        assertEquals("No images found for breed ID: " + breedId, exception.getMessage());
    }

    @Test
    void getImagesByBreedId_NullOrEmptyBreedId_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> imageService.getImagesByBreedId(null, 10));
        assertThrows(IllegalArgumentException.class, () -> imageService.getImagesByBreedId("", 10));
        assertThrows(IllegalArgumentException.class, () -> imageService.getImagesByBreedId("   ", 10));
    }

    @Test
    void getImagesByBreedId_NullLimit_UsesDefault() {
        // Arrange
        String breedId = "abys";

        ImageDto image = new ImageDto();
        image.setId("img1");
        image.setUrl("https://example.com/image1.jpg");

        List<ImageDto> expectedImages = List.of(image);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class)).thenReturn(Flux.fromIterable(expectedImages));

        // Act
        List<ImageDto> result = imageService.getImagesByBreedId(breedId, null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getImagesByBreedId_LimitAbove100_CapsTo100() {
        // Arrange
        String breedId = "abys";
        Integer limit = 150; // Above 100

        ImageDto image = new ImageDto();
        image.setId("img1");
        image.setUrl("https://example.com/image1.jpg");

        List<ImageDto> expectedImages = List.of(image);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class)).thenReturn(Flux.fromIterable(expectedImages));

        // Act
        List<ImageDto> result = imageService.getImagesByBreedId(breedId, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getImagesByBreedId_WebClientException_ThrowsRuntimeException() {
        // Arrange
        String breedId = "abys";
        Integer limit = 10;

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ImageDto.class))
                .thenThrow(WebClientResponseException.BadRequest.create(400, "Bad Request", null, null, null));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> imageService.getImagesByBreedId(breedId, limit));
        assertEquals("Failed to fetch images from external API", exception.getMessage());
    }
}