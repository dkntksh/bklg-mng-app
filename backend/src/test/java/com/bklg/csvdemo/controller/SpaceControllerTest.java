package com.bklg.csvdemo.controller;

import net.minidev.json.JSONObject;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpaceControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void registSpace(){
        String body = "{\"spaceId\":\"kzyizk\",\"apiKey\":\"abcd123456\",\"url\":\"kzyizkbacklog.co.jp\",\"projectKey\":\"projectKey-ab\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        ResponseEntity actual = testRestTemplate.exchange(
                "/api/v1/space", HttpMethod.POST, new HttpEntity<>(body, headers), String.class);

        // assertThat(actual.getBody().toString(), is("ConstraintViolationException"));
        // assertThat(actual.getBody().toString(), is("ok"));
        assertThat(actual.getStatusCode(), is(HttpStatus.CREATED));
    }


}
