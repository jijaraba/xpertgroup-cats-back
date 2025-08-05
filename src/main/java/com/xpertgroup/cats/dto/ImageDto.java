package com.xpertgroup.cats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for Cat Image information from external API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDto {
    
    private String id;
    private String url;
    private Integer width;
    private Integer height;
    
    @JsonProperty("breed_ids")
    private String[] breedIds;
    
    // Default constructor
    public ImageDto() {}
    
    // Constructor with essential fields
    public ImageDto(String id, String url, Integer width, Integer height) {
        this.id = id;
        this.url = url;
        this.width = width;
        this.height = height;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Integer getWidth() {
        return width;
    }
    
    public void setWidth(Integer width) {
        this.width = width;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public String[] getBreedIds() {
        return breedIds;
    }
    
    public void setBreedIds(String[] breedIds) {
        this.breedIds = breedIds;
    }
}