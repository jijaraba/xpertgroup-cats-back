package com.xpertgroup.cats.service;

import com.xpertgroup.cats.dto.ImageDto;
import com.xpertgroup.cats.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * Service class for cat image operations
 */
@Service
public class ImageService {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    
    private final WebClient catApiWebClient;
    
    public ImageService(@Qualifier("catApiWebClient") WebClient catApiWebClient) {
        this.catApiWebClient = catApiWebClient;
    }
    
    /**
     * Get images by breed ID
     * @param breedId the breed ID to get images for
     * @param limit maximum number of images to return (default 10)
     * @return List of images for the specified breed
     * @throws NotFoundException if no images are found for the breed
     */
    public List<ImageDto> getImagesByBreedId(String breedId, Integer limit) {
        logger.info("Fetching images for breed ID: {} with limit: {}", breedId, limit);
        
        if (breedId == null || breedId.trim().isEmpty()) {
            throw new IllegalArgumentException("Breed ID cannot be null or empty");
        }
        
        // Set default limit if not provided
        if (limit == null || limit <= 0) {
            limit = 10;
        }
        
        // Cap the limit to prevent excessive API calls
        if (limit > 100) {
            limit = 100;
        }
        
        // Final variable for lambda
        final Integer finalLimit = limit;
        
        try {
            List<ImageDto> images = catApiWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/images/search")
                            .queryParam("breed_ids", breedId.trim())
                            .queryParam("limit", finalLimit)
                            .queryParam("has_breeds", true)
                            .build())
                    .retrieve()
                    .bodyToFlux(ImageDto.class)
                    .collectList()
                    .block();
            
            if (images == null || images.isEmpty()) {
                logger.warn("No images found for breed ID: {}", breedId);
                throw new NotFoundException("No images found for breed ID: " + breedId);
            }
            
            logger.info("Successfully fetched {} images for breed ID: {}", images.size(), breedId);
            return images;
            
        } catch (NotFoundException ex) {
            // Re-throw NotFoundException as-is
            throw ex;
        } catch (WebClientResponseException ex) {
            logger.error("Error fetching images for breed {} from external API: {}", breedId, ex.getMessage());
            throw new RuntimeException("Failed to fetch images from external API", ex);
        } catch (Exception ex) {
            logger.error("Unexpected error while fetching images for breed {}: {}", breedId, ex.getMessage());
            throw new RuntimeException("Unexpected error occurred while fetching images", ex);
        }
    }
    
    /**
     * Get images by breed ID with default limit
     * @param breedId the breed ID to get images for
     * @return List of images for the specified breed
     */
    public List<ImageDto> getImagesByBreedId(String breedId) {
        return getImagesByBreedId(breedId, 10);
    }
}