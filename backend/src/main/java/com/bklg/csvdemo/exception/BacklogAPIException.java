package com.bklg.csvdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * BacklogAPI実行時のエラー
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BacklogAPIException extends Exception{

    public BacklogAPIException() {
        super("backlog api error");
    }
    
    public BacklogAPIException(String message) {
      super(message);
    }
}
