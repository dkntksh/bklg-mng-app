package com.bklg.csvdemo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class SpaceUpdateRequestDto {

    @NotEmpty(message = "spaceId is empty")
    private String spaceId;  
    
    @NotEmpty(message = "apiKey is empty")
    private String apiKey;

    @NotEmpty(message = "domain is empty")
    private String domain;

    @NotEmpty(message = "projectKey is empty")
    private String projectKey;

    private Long projectId;
}
