package com.bklg.csvdemo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.bklg.csvdemo.service.EncryptDecriptService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "spaces")
@Table(name = "spaces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Space implements Serializable {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotEmpty(message = "spaceId is empty")
    @NotNull(message = "spaceId is null")
    @Column(nullable = false)
    private String spaceId;
    
    
    @NotEmpty(message = "apiKey is empty")
    @NotNull(message = "apiKey is null")
    @Column(nullable = false)
    private String apiKey;

    
    @NotEmpty(message = "domain is empty")
    @NotNull(message = "domain is null")
    @Column(nullable = false)
    private String domain;

    
    @NotEmpty(message = "projectKey is empty")
    @NotNull(message = "projectKey is null")
    @Column(nullable = false)
    private String projectKey;

    @Column(nullable = false)
    private Long projectId;

    /**
     * 復号したAPIKeyを返却する
     * @param encryptDecriptService
     * @return
     */
    public String getDecryptAPIKey(EncryptDecriptService encryptDecriptService){
        return encryptDecriptService.decrypt(this.apiKey);
    }
}
