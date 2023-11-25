package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.model.planday.DepartmentResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.TokenRequest;
import com.resort.platform.backnode.foodtracker.model.planday.TokenResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.UserResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdministrationService {

    Logger logger = LoggerFactory.getLogger(AdministrationService.class);
    public String getToken(String urlString, String token, String clientid) throws IOException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(urlString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id",clientid);
        map.add("grant_type","refresh_token");
        map.add("refresh_token",token);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<TokenResponseModel> tokenResponseModel = restTemplate.exchange(uri,
                HttpMethod.POST,
                entity,
                TokenResponseModel.class);

        return Objects.requireNonNull(tokenResponseModel.getBody()).getAccess_token();
    }

    public DepartmentResponseModel getDepartments(String urlString, String token, String clientid) throws IOException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(urlString);
        HttpHeaders headers = new HttpHeaders();
        headers.add("client_id",clientid);
        headers.add("Authorization", "Bearer " +token);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
        ResponseEntity<DepartmentResponseModel> responseModelResponseEntity = restTemplate.exchange(uri,
                HttpMethod.GET,
                entity,
                DepartmentResponseModel.class);

        return Objects.requireNonNull(responseModelResponseEntity.getBody());
    }
    public UserResponseModel getEmployees(String urlString, String token, String clientid, int offset) throws IOException, URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI(urlString+"?offset="+String.valueOf(offset));
        HttpHeaders headers = new HttpHeaders();
        headers.add("client_id",clientid);
        headers.add("Authorization", "Bearer " +token);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
        ResponseEntity<UserResponseModel> responseModelResponseEntity = restTemplate.exchange(uri,
                HttpMethod.GET,
                entity,
                UserResponseModel.class);

        return Objects.requireNonNull(responseModelResponseEntity.getBody());
    }
}
