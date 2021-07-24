package com.bklg.csvdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ファイルアップロード時のチェックで発生したエラーの例外
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileUploadErrorException extends Exception{
    public FileUploadErrorException() {
        super("file check error");
    }
    
    public FileUploadErrorException(String message) {
      super(message);
    }
}
