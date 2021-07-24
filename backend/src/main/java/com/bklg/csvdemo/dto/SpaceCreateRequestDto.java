package com.bklg.csvdemo.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceCreateRequestDto {

    @NotEmpty(message = "spaceId is empty")
    @NotNull(message = "spaceId is null")
    private String spaceId;
    
    
    @NotEmpty(message = "apiKey is empty")
    @NotNull(message = "apiKey is null")
    private String apiKey;

    
    @NotEmpty(message = "domain is empty")
    @NotNull(message = "String is null")
    private String domain;

    
    @NotEmpty(message = "projectKey is empty")
    @NotNull(message = "String is null")
    private String projectKey;


    private Long projectId;
}
