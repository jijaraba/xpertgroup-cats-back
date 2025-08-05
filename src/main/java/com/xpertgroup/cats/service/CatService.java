package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.BreedDto;
import com.xpertgroup.cats.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * Service class for cat breed operations
 */
@Service
public class CatService {
    
    private static final Logger logger = LoggerFactory.getLogger(CatService.class);
    
    private final WebClient catApiWebClient;
    
    public CatService(@Qualifier("catApiWebClient") WebClient catApiWebClient) {
        this.catApiWebClient = catApiWebClient;
    }
    
    /**
     * Get all cat breeds
     * @return List of all cat breeds
     */
    public List<BreedDto> getAllBreeds() {
        logger.info("Fetching all cat breeds from external API");
        
        try {
            List<BreedDto> breeds = catApiWebClient
                    .get()
                    .uri("/breeds")
                    .retrieve()
                    .bodyToFlux(BreedDto.class)
                    .collectList()
                    .block();
            
            if (breeds == null || breeds.isEmpty()) {
                logger.warn("No breeds found in external API");
                throw new NotFoundException("No cat breeds found");
            }
            
            logger.info("Successfully fetched {} cat breeds", breeds.size());
            return breeds;
            
        } catch (NotFoundException ex) {
            // Re-throw NotFoundException as-is
            throw ex;
        } catch (WebClientResponseException ex) {
            logger.error("Error fetching breeds from external API: {}", ex.getMessage());
            throw new RuntimeException("Failed to fetch cat breeds from external API", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching breeds: {}", ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while fetching cat breeds", ex);
        }
    }
    
    /**
     * Get a specific cat breed by ID
     * @param breedId the breed ID to search for
     * @return The breed information
     * @throws NotFoundException if breed is not found
     */
    public BreedDto getBreedById(String breedId) {
        logger.info("Fetching cat breed with ID: {}", breedId);
        
        if (breedId == null || breedId.trim().isEmpty()) {
            throw new IllegalArgumentException("Breed ID cannot be null or empty");
        }
        
        try {
            BreedDto breed = catApiWebClient
                    .get()
                    .uri("/breeds/{breedId}", breedId)
                    .retrieve()
                    .bodyToMono(BreedDto.class)
                    .block();
            
            if (breed == null) {
                logger.warn("Breed not found with ID: {}", breedId);
                throw new NotFoundException("Cat breed not found with ID: " + breedId);
            }
            
            logger.info("Successfully fetched breed: {}", breed.getName());
            return breed;
            
        } catch (WebClientResponseException.NotFound ex) {
            logger.warn("Breed not found with ID: {}", breedId);
            throw new NotFoundException("Cat breed not found with ID: " + breedId);
        } catch (WebClientResponseException ex) {
            logger.error("Error fetching breed {} from external API: {}", breedId, ex.getMessage());
            throw new RuntimeException("Failed to fetch cat breed from external API", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching breed {}: {}", breedId, ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while fetching cat breed", ex);
        }
    }
    
    /**
     * Search cat breeds based on query parameter
     * @param query the search query
     * @return List of breeds matching the search criteria
     */
    public List<BreedDto> searchBreeds(String query) {
        logger.info("Searching cat breeds with query: {}", query);
        
        if (query == null || query.trim().isEmpty()) {
            logger.info("Empty search query, returning all breeds");
            return getAllBreeds();
        }
        
        try {
            List<BreedDto> breeds = catApiWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/breeds/search")
                            .queryParam("q", query.trim())
                            .build())
                    .retrieve()
                    .bodyToFlux(BreedDto.class)
                    .collectList()
                    .block();
            
            if (breeds == null) {
                breeds = List.of();
            }
            
            logger.info("Found {} breeds matching query: {}", breeds.size(), query);
            return breeds;
            
        } catch (WebClientResponseException ex) {
            logger.error("Error searching breeds with query '{}' from external API: {}", query, ex.getMessage());
            throw new RuntimeException("Failed to search cat breeds from external API", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error while searching breeds with query '{}': {}", query, ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while searching cat breeds", ex);
        }
    }
}