package com.bklg.csvdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 課題の種別の必要なResponse
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseIssueType {
    private String id;

    private String name;
}
