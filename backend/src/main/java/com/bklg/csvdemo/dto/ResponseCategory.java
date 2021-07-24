package com.bklg.csvdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategory {
    private String id;

    private String name;

    private String displayOrder;
}
