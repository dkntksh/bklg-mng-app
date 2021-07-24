package com.bklg.csvdemo.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpaceUnlessApiKeyDto implements Serializable {

    private int id;

    private String spaceId;

    private String domain;

    private String projectKey;
}
