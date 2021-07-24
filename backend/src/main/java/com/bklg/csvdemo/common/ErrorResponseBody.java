package com.bklg.csvdemo.common;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;

@Setter
public class ErrorResponseBody {
  @JsonProperty("timestamp")
  private ZonedDateTime exceptionOccurrenceTime;
  @JsonProperty("status")
  private int status;
  @JsonProperty("error")
  private String error;
  @JsonProperty("message")
  private List<String> message;
  @JsonProperty("path")
  private String path;
}