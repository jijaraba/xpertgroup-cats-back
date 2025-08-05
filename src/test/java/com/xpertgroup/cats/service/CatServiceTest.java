package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.BreedDto;
import com.xpertgroup.cats.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatServiceTest {

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

    private CatService catService;

    @BeforeEach
    void setUp() {
        catService = new CatService(webClient);
    }

    @Test
    void getAllBreeds_Success() {
        // Arrange
        BreedDto breed1 = new BreedDto();
        breed1.setId("abys");
        breed1.setName("Abyssinian");

        BreedDto breed2 = new BreedDto();
        breed2.setId("aege");
        breed2.setName("Aegean");

        List<BreedDto> expectedBreeds = List.of(breed1, breed2);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BreedDto.class)).thenReturn(Flux.fromIterable(expectedBreeds));

        // Act
        List<BreedDto> result = catService.getAllBreeds();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Abyssinian", result.get(0).getName());
        assertEquals("Aegean", result.get(1).getName());

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/breeds");
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToFlux(BreedDto.class);
    }

    @Test
    void getAllBreeds_EmptyList_ThrowsNotFoundException() {
        // Arrange
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BreedDto.class)).thenReturn(Flux.fromIterable(Collections.emptyList()));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> catService.getAllBreeds());
        assertEquals("No cat breeds found", exception.getMessage());
    }

    @Test
    void getBreedById_Success() {
        // Arrange
        String breedId = "abys";
        BreedDto expectedBreed = new BreedDto();
        expectedBreed.setId(breedId);
        expectedBreed.setName("Abyssinian");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds/{breedId}", breedId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BreedDto.class)).thenReturn(Mono.just(expectedBreed));

        // Act
        BreedDto result = catService.getBreedById(breedId);

        // Assert
        assertNotNull(result);
        assertEquals(breedId, result.getId());
        assertEquals("Abyssinian", result.getName());

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri("/breeds/{breedId}", breedId);
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToMono(BreedDto.class);
    }

    @Test
    void getBreedById_NotFound_ThrowsNotFoundException() {
        // Arrange
        String breedId = "invalid";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds/{breedId}", breedId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(BreedDto.class))
                .thenThrow(WebClientResponseException.NotFound.create(404, "Not Found", null, null, null));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> catService.getBreedById(breedId));
        assertEquals("Cat breed not found with ID: " + breedId, exception.getMessage());
    }

    @Test
    void getBreedById_NullOrEmptyId_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> catService.getBreedById(null));
        assertThrows(IllegalArgumentException.class, () -> catService.getBreedById(""));
        assertThrows(IllegalArgumentException.class, () -> catService.getBreedById("   "));
    }

    @Test
    void searchBreeds_Success() {
        // Arrange
        String query = "persian";
        BreedDto breed = new BreedDto();
        breed.setId("pers");
        breed.setName("Persian");

        List<BreedDto> expectedBreeds = List.of(breed);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BreedDto.class)).thenReturn(Flux.fromIterable(expectedBreeds));

        // Act
        List<BreedDto> result = catService.searchBreeds(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Persian", result.get(0).getName());

        verify(webClient).get();
        verify(requestHeadersUriSpec).uri(any(java.util.function.Function.class));
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).bodyToFlux(BreedDto.class);
    }

    @Test
    void searchBreeds_EmptyQuery_CallsGetAllBreeds() {
        // Arrange
        BreedDto breed = new BreedDto();
        breed.setId("abys");
        breed.setName("Abyssinian");

        List<BreedDto> expectedBreeds = List.of(breed);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/breeds")).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BreedDto.class)).thenReturn(Flux.fromIterable(expectedBreeds));

        // Act
        List<BreedDto> result = catService.searchBreeds("");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Abyssinian", result.get(0).getName());
    }

    @Test
    void searchBreeds_WebClientException_ThrowsRuntimeException() {
        // Arrange
        String query = "test";

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(java.util.function.Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(BreedDto.class))
                .thenThrow(WebClientResponseException.BadRequest.create(400, "Bad Request", null, null, null));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> catService.searchBreeds(query));
        assertEquals("Failed to search cat breeds from external API", exception.getMessage());
    }
}