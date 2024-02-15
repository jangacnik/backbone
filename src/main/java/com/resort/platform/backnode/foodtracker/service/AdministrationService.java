package com.resort.platform.backnode.foodtracker.service;

import com.resort.platform.backnode.foodtracker.model.planday.DepartmentResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.ShiftsModel;
import com.resort.platform.backnode.foodtracker.model.planday.TokenResponseModel;
import com.resort.platform.backnode.foodtracker.model.planday.UserResponseModel;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class AdministrationService {

  Logger logger = LoggerFactory.getLogger(AdministrationService.class);

  /**
   * Gets access token for accessing the Planday API
   *
   * @param urlString - URL to the access point
   * @param token     - API token
   * @param clientid  - ID of the company
   * @return - API Access Token
   * @throws IOException
   * @throws URISyntaxException
   */
  public String getToken(String urlString, String token, String clientid)
      throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(urlString);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", clientid);
    map.add("grant_type", "refresh_token");
    map.add("refresh_token", token);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
    ResponseEntity<TokenResponseModel> tokenResponseModel = restTemplate.exchange(uri,
        HttpMethod.POST,
        entity,
        TokenResponseModel.class);
    logger.info("Successfully recieved access token for Planday");
    return Objects.requireNonNull(tokenResponseModel.getBody()).getAccess_token();
  }

  /**
   * Returns all departments of the company in planday
   *
   * @param urlString - department URL
   * @param token     - access token
   * @param clientid  - ID of company
   * @return list of all departments in Planday
   * @throws IOException
   * @throws URISyntaxException
   */
  public DepartmentResponseModel getDepartments(String urlString, String token, String clientid)
      throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(urlString);
    HttpHeaders headers = new HttpHeaders();
    headers.add("client_id", clientid);
    headers.add("Authorization", "Bearer " + token);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
    ResponseEntity<DepartmentResponseModel> responseModelResponseEntity = restTemplate.exchange(uri,
        HttpMethod.GET,
        entity,
        DepartmentResponseModel.class);

    return Objects.requireNonNull(responseModelResponseEntity.getBody());
  }

  /**
   * Gets employees from Planday with an offset from 0, returns 50 users by default
   *
   * @param urlString - employee API url
   * @param token     - access token
   * @param clientid  - ID of the company
   * @param offset    - offset from index 0
   * @return List of employess (max size 50)
   * @throws IOException        -
   * @throws URISyntaxException - REST call exception
   */
  public UserResponseModel getEmployees(String urlString, String token, String clientid, int offset)
      throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(urlString + "?offset=" + String.valueOf(offset));
    HttpHeaders headers = new HttpHeaders();
    headers.add("client_id", clientid);
    headers.add("Authorization", "Bearer " + token);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
    ResponseEntity<UserResponseModel> responseModelResponseEntity = restTemplate.exchange(uri,
        HttpMethod.GET,
        entity,
        UserResponseModel.class);

    return Objects.requireNonNull(responseModelResponseEntity.getBody());
  }

  public ShiftsModel getAvailableEmployees(String urlString, String token, String clientid, String depId, String date)
      throws IOException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    URI uri = new URI(urlString+depId + "&from=" + date + "&to=" +date + "&shiftStatus=Assigned");
    HttpHeaders headers = new HttpHeaders();
    headers.add("client_id", clientid);
    headers.add("Authorization", "Bearer " + token);
    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);
    ResponseEntity<ShiftsModel> responseModelResponseEntity = restTemplate.exchange(uri,
        HttpMethod.GET,
        entity,
        ShiftsModel.class);

    return Objects.requireNonNull(responseModelResponseEntity.getBody());
  }
}
