package com.xpertgroup.cats.controller;

import com.xpertgroup.cats.dto.BreedDto;
import com.xpertgroup.cats.service.CatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for cat breed operations
 */
@RestController
@RequestMapping("/breeds")
public class CatController {
    
    private static final Logger logger = LoggerFactory.getLogger(CatController.class);
    
    private final CatService catService;
    
    public CatController(CatService catService) {
        this.catService = catService;
    }
    
    /**
     * Get all cat breeds
     * @return List of all cat breeds
     */
    @GetMapping
    public ResponseEntity<List<BreedDto>> getAllBreeds() {
        logger.info("GET /breeds - Fetching all cat breeds");
        
        List<BreedDto> breeds = catService.getAllBreeds();
        
        logger.info("GET /breeds - Successfully returned {} breeds", breeds.size());
        return ResponseEntity.ok(breeds);
    }
    
    /**
     * Get a specific cat breed by ID
     * @param breedId the breed ID
     * @return The breed information
     */
    @GetMapping("/{breedId}")
    public ResponseEntity<BreedDto> getBreedById(@PathVariable String breedId) {
        logger.info("GET /breeds/{} - Fetching breed by ID", breedId);
        
        BreedDto breed = catService.getBreedById(breedId);
        
        logger.info("GET /breeds/{} - Successfully returned breed: {}", breedId, breed.getName());
        return ResponseEntity.ok(breed);
    }
    
    /**
     * Search cat breeds based on query parameter
     * @param query the search query (optional)
     * @return List of breeds matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<BreedDto>> searchBreeds(@RequestParam(value = "q", required = false) String query) {
        logger.info("GET /breeds/search - Searching breeds with query: {}", query);
        
        List<BreedDto> breeds = catService.searchBreeds(query);
        
        logger.info("GET /breeds/search - Successfully returned {} breeds for query: {}", breeds.size(), query);
        return ResponseEntity.ok(breeds);
    }
}