package com.bklg.csvdemo.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.bklg.csvdemo.config.SecurityValueConfig;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 暗号化、復号を担うサービス
 */
@Service
public class EncryptDecriptService {

    static final String ENCRYPT_ALGORITHM = "AES";

    static char PADDING_CHAR = '\034';

    @Autowired
    private SecurityValueConfig securityValueConfig;

    /**
     * 暗号化
     * @param tartgetStr
     * @return
     */
    public String encrypt(String tartgetStr){
        if(StringUtils.isEmpty(tartgetStr)){
            return "";
        }
        String retVal = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(securityValueConfig.getKey().getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(securityValueConfig.getInit_vector().getBytes(StandardCharsets.UTF_8)));
            int paddingSize = 16 - tartgetStr.length() % 16;
            String padding = String.format("%0" + paddingSize + "d", 0).replace('0', PADDING_CHAR);
            String padded = tartgetStr + padding;
            byte[] encrypted = cipher.doFinal(padded.getBytes(StandardCharsets.UTF_8));
            retVal = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    /**
     * 復号
     * @param tartgetStr
     * @return
     */
    public String decrypt(String tartgetStr){
        if(StringUtils.isEmpty(tartgetStr)){
            return "";
        }
        String retVal = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            SecretKeySpec key = new SecretKeySpec(securityValueConfig.getKey().getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(securityValueConfig.getInit_vector().getBytes(StandardCharsets.UTF_8)));
            byte[] encrypted = Base64.getDecoder().decode(tartgetStr);
            String padded = new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
            retVal = padded.replaceAll(PADDING_CHAR + "+$", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }


}
