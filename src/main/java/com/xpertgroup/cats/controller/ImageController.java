package com.xpertgroup.cats.controller;

import com.xpertgroup.cats.dto.ImageDto;
import com.xpertgroup.cats.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for cat image operations
 */
@RestController
@RequestMapping("/imagesbybreedid")
public class ImageController {
    
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    
    private final ImageService imageService;
    
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
    
    /**
     * Get images by breed ID
     * @param breedId the breed ID to get images for (required)
     * @param limit maximum number of images to return (optional, default 10)
     * @return List of images for the specified breed
     */
    @GetMapping
    public ResponseEntity<List<ImageDto>> getImagesByBreedId(
            @RequestParam("breed_id") String breedId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {
        
        logger.info("GET /imagesbybreedid - Fetching images for breed: {} with limit: {}", breedId, limit);
        
        List<ImageDto> images = imageService.getImagesByBreedId(breedId, limit);
        
        logger.info("GET /imagesbybreedid - Successfully returned {} images for breed: {}", images.size(), breedId);
        return ResponseEntity.ok(images);
    }
}