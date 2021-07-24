package com.bklg.csvdemo.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
/**
 * スペース情報リストのレスポンスクラス
 */
public class SpaceListResponse  implements Serializable {
    @JsonProperty(value = "total", required = true)
    private int total;

    @JsonProperty(value = "spaces", required = true)
    private List<SpaceUnlessApiKeyDto> spaceList;

}
