package com.xpertgroup.cats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for Cat Breed information from external API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BreedDto {
    
    private String id;
    private String name;
    private String description;
    private String temperament;
    private String origin;
    
    @JsonProperty("life_span")
    private String lifeSpan;
    
    @JsonProperty("indoor")
    private Integer indoor;
    
    @JsonProperty("lap")
    private Integer lap;
    
    @JsonProperty("alt_names")
    private String altNames;
    
    @JsonProperty("adaptability")
    private Integer adaptability;
    
    @JsonProperty("affection_level")
    private Integer affectionLevel;
    
    @JsonProperty("child_friendly")
    private Integer childFriendly;
    
    @JsonProperty("dog_friendly")
    private Integer dogFriendly;
    
    @JsonProperty("energy_level")
    private Integer energyLevel;
    
    @JsonProperty("grooming")
    private Integer grooming;
    
    @JsonProperty("health_issues")
    private Integer healthIssues;
    
    @JsonProperty("intelligence")
    private Integer intelligence;
    
    @JsonProperty("shedding_level")
    private Integer sheddingLevel;
    
    @JsonProperty("social_needs")
    private Integer socialNeeds;
    
    @JsonProperty("stranger_friendly")
    private Integer strangerFriendly;
    
    @JsonProperty("vocalisation")
    private Integer vocalisation;
    
    @JsonProperty("experimental")
    private Integer experimental;
    
    @JsonProperty("hairless")
    private Integer hairless;
    
    @JsonProperty("natural")
    private Integer natural;
    
    @JsonProperty("rare")
    private Integer rare;
    
    @JsonProperty("rex")
    private Integer rex;
    
    @JsonProperty("suppressed_tail")
    private Integer suppressedTail;
    
    @JsonProperty("short_legs")
    private Integer shortLegs;
    
    @JsonProperty("wikipedia_url")
    private String wikipediaUrl;
    
    @JsonProperty("hypoallergenic")
    private Integer hypoallergenic;
    
    @JsonProperty("reference_image_id")
    private String referenceImageId;
    
    @JsonProperty("cfa_url")
    private String cfaUrl;
    
    @JsonProperty("vetstreet_url")
    private String vetstreetUrl;
    
    @JsonProperty("vcahospitals_url")
    private String vcahospitalsUrl;
    
    // Default constructor
    public BreedDto() {}
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTemperament() {
        return temperament;
    }
    
    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getLifeSpan() {
        return lifeSpan;
    }
    
    public void setLifeSpan(String lifeSpan) {
        this.lifeSpan = lifeSpan;
    }
    
    public Integer getIndoor() {
        return indoor;
    }
    
    public void setIndoor(Integer indoor) {
        this.indoor = indoor;
    }
    
    public Integer getLap() {
        return lap;
    }
    
    public void setLap(Integer lap) {
        this.lap = lap;
    }
    
    public String getAltNames() {
        return altNames;
    }
    
    public void setAltNames(String altNames) {
        this.altNames = altNames;
    }
    
    public Integer getAdaptability() {
        return adaptability;
    }
    
    public void setAdaptability(Integer adaptability) {
        this.adaptability = adaptability;
    }
    
    public Integer getAffectionLevel() {
        return affectionLevel;
    }
    
    public void setAffectionLevel(Integer affectionLevel) {
        this.affectionLevel = affectionLevel;
    }
    
    public Integer getChildFriendly() {
        return childFriendly;
    }
    
    public void setChildFriendly(Integer childFriendly) {
        this.childFriendly = childFriendly;
    }
    
    public Integer getDogFriendly() {
        return dogFriendly;
    }
    
    public void setDogFriendly(Integer dogFriendly) {
        this.dogFriendly = dogFriendly;
    }
    
    public Integer getEnergyLevel() {
        return energyLevel;
    }
    
    public void setEnergyLevel(Integer energyLevel) {
        this.energyLevel = energyLevel;
    }
    
    public Integer getGrooming() {
        return grooming;
    }
    
    public void setGrooming(Integer grooming) {
        this.grooming = grooming;
    }
    
    public Integer getHealthIssues() {
        return healthIssues;
    }
    
    public void setHealthIssues(Integer healthIssues) {
        this.healthIssues = healthIssues;
    }
    
    public Integer getIntelligence() {
        return intelligence;
    }
    
    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }
    
    public Integer getSheddingLevel() {
        return sheddingLevel;
    }
    
    public void setSheddingLevel(Integer sheddingLevel) {
        this.sheddingLevel = sheddingLevel;
    }
    
    public Integer getSocialNeeds() {
        return socialNeeds;
    }
    
    public void setSocialNeeds(Integer socialNeeds) {
        this.socialNeeds = socialNeeds;
    }
    
    public Integer getStrangerFriendly() {
        return strangerFriendly;
    }
    
    public void setStrangerFriendly(Integer strangerFriendly) {
        this.strangerFriendly = strangerFriendly;
    }
    
    public Integer getVocalisation() {
        return vocalisation;
    }
    
    public void setVocalisation(Integer vocalisation) {
        this.vocalisation = vocalisation;
    }
    
    public Integer getExperimental() {
        return experimental;
    }
    
    public void setExperimental(Integer experimental) {
        this.experimental = experimental;
    }
    
    public Integer getHairless() {
        return hairless;
    }
    
    public void setHairless(Integer hairless) {
        this.hairless = hairless;
    }
    
    public Integer getNatural() {
        return natural;
    }
    
    public void setNatural(Integer natural) {
        this.natural = natural;
    }
    
    public Integer getRare() {
        return rare;
    }
    
    public void setRare(Integer rare) {
        this.rare = rare;
    }
    
    public Integer getRex() {
        return rex;
    }
    
    public void setRex(Integer rex) {
        this.rex = rex;
    }
    
    public Integer getSuppressedTail() {
        return suppressedTail;
    }
    
    public void setSuppressedTail(Integer suppressedTail) {
        this.suppressedTail = suppressedTail;
    }
    
    public Integer getShortLegs() {
        return shortLegs;
    }
    
    public void setShortLegs(Integer shortLegs) {
        this.shortLegs = shortLegs;
    }
    
    public String getWikipediaUrl() {
        return wikipediaUrl;
    }
    
    public void setWikipediaUrl(String wikipediaUrl) {
        this.wikipediaUrl = wikipediaUrl;
    }
    
    public Integer getHypoallergenic() {
        return hypoallergenic;
    }
    
    public void setHypoallergenic(Integer hypoallergenic) {
        this.hypoallergenic = hypoallergenic;
    }
    
    public String getReferenceImageId() {
        return referenceImageId;
    }
    
    public void setReferenceImageId(String referenceImageId) {
        this.referenceImageId = referenceImageId;
    }
    
    public String getCfaUrl() {
        return cfaUrl;
    }
    
    public void setCfaUrl(String cfaUrl) {
        this.cfaUrl = cfaUrl;
    }
    
    public String getVetstreetUrl() {
        return vetstreetUrl;
    }
    
    public void setVetstreetUrl(String vetstreetUrl) {
        this.vetstreetUrl = vetstreetUrl;
    }
    
    public String getVcahospitalsUrl() {
        return vcahospitalsUrl;
    }
    
    public void setVcahospitalsUrl(String vcahospitalsUrl) {
        this.vcahospitalsUrl = vcahospitalsUrl;
    }
}