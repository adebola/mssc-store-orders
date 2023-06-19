package io.factorialsystems.msscstoreorders.utils;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class KeyCloak {
    final String client_id = "public-client";
    final String password = "password";
    final String user = "adeomoboya@googlemail.com";
    final String authUrl = "http://localhost:8080/auth/realms/onecard/protocol/openid-connect/token";


//    public String getUserToken(String userId) {
//
//        String realmToken = getRealmAdminToken();
//
//        if (realmToken == null) {
//            return null;
//        }
//
//        // Now Get the User Token
//        return getUserToken(userId, realmToken);
//    }

    public String getUserToken() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        requestBody.add("client_id", client_id);
        requestBody.add("grant_type", "password");
        requestBody.add("password", password);
        requestBody.add("username", user);
        requestBody.add("scope", "openid");

        // Get the Realm Administrator Token
        return getToken(requestBody);
    }

//    private String getUserToken(String userId, String realmToken) {
//        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
//        requestBody.add("client_id", client_id);
//        requestBody.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
//        requestBody.add("subject_token", realmToken);
//        requestBody.add("requested_subject", userId);
//
//        return getToken(requestBody);
//    }

    private String getToken(MultiValueMap<String, String> requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<TokenResponseDto> response =
                restTemplate.exchange(authUrl, HttpMethod.POST, formEntity, TokenResponseDto.class);

        TokenResponseDto token = response.getBody();

        if (token == null || token.getAccess_token() == null || token.getAccess_token().length() < 1) {
            return null;
        }

        return token.getAccess_token();
    }
}
