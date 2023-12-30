package com.heygongc.user.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heygongc.user.presentation.response.GoogleUserResponse;
import com.heygongc.user.presentation.response.GoogleTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class GoogleOAuth {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${google.client.id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.client.secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${google.client.redirect-uri}")
    private String GOOGLE_LOGIN_REDIRECT_URI;

    public String getGoogleLoginUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth?"
                + "client_id=" + GOOGLE_CLIENT_ID
                + "&redirect_uri=" + GOOGLE_LOGIN_REDIRECT_URI
                + "&response_type=code"
                + "&scope=email profile"
                + "&access_type=offline"
                + "&prompt=consent";
    }

    public ResponseEntity<String> requestAccessToken(String authCode) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", authCode);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_LOGIN_REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.debug("googleOAuth requestAccessToken response.getBody() >> {}", responseEntity.getBody());
            return responseEntity;
        }
        return null;
    }

    public GoogleTokenResponse getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), GoogleTokenResponse.class);
    }

    public ResponseEntity<String> requestUserInfo(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://oauth2.googleapis.com/tokeninfo", HttpMethod.GET, request, String.class);

        logger.debug("googleOAuth requestUserInfo response.getBody() >> {}", responseEntity.getBody());
        return responseEntity;
    }

    public GoogleUserResponse getUserInfo(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), GoogleUserResponse.class);
    }
}