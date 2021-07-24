package com.bklg.csvdemo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * レスポンスのエラー時に返却する共通クラス
 */
public class ResponseError {
    private int status;
    private String message;
}
