package com.bklg.csvdemo.controller;


import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import com.bklg.csvdemo.common.ErrorResponseBody;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;



@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleMyException(Exception exception, WebRequest request) {
      HttpHeaders headers = new HttpHeaders();
      exception.printStackTrace();

      return super.handleExceptionInternal(exception,
              createErrorResponseBody(exception, request),
              headers,
              HttpStatus.BAD_REQUEST,
              request);
  }

  // レスポンスのボディ部を作成
  private ErrorResponseBody createErrorResponseBody(Exception exception, WebRequest request) {
    ErrorResponseBody errorResponseBody = new ErrorResponseBody();
    int responseCode = HttpStatus.BAD_REQUEST.value();
    String responseErrorMessage = HttpStatus.BAD_REQUEST.getReasonPhrase();
    String uri = ((ServletWebRequest) request).getRequest().getRequestURI();

    errorResponseBody.setExceptionOccurrenceTime(ZonedDateTime.now());
    errorResponseBody.setStatus(responseCode);
    errorResponseBody.setError(responseErrorMessage);
    List<String> errorMsgList = Arrays.asList(exception.getMessage().replace("[", "").replace("]", "").split(","));
    errorResponseBody.setMessage(errorMsgList);
    errorResponseBody.setPath(uri);
    return errorResponseBody;
  }
}
