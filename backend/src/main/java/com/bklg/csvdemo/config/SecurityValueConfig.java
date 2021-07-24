package com.bklg.csvdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityValueConfig {

    /**
     * 暗号化キー
     */
    private String key;

    /**
     * 初期化ベクトル
     */
    private String init_vector;
}
