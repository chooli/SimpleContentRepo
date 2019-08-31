package com.jumkid.media.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
@Component("oauthTokenManager")
public class OAuthTokenManager {

    @Value("${oauthservice.token.get.url}")
    private String getTokenUrl;

    @Value("${oauthservice.token.check.url}")
    private String checkTokenUrl;

    @Value("${oauthservice.client.id.name}")
    private String clientIdName;

    @Value("${oauthservice.client.id.value}")
    private String clientIdValue;

    @Value("${oauthservice.client.secret.name}")
    private String clientSecretName;

    @Value("${oauthservice.client.secret.value}")
    private String clientSecretValue;

    @Value("${oauthservice.client.grant.name}")
    private String clientGrantName;

    @Value("${oauthservice.client.grant.value}")
    private String clientGrantValue;

    private String accessToken;

    private final RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OAuthTokenManager.class);

    @Autowired
    public OAuthTokenManager(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateCurrentToken(){
        if(accessToken != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            try {
                OAuthTokenResponse response = restTemplate.postForObject(checkTokenUrl, request, OAuthTokenResponse.class);
                if(response!=null && response.getError() == null && response.getClientId().equals(clientIdValue)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("failed to fetch access token from OAuth Service {}", e.getMessage());
            }
        }

        return false;
    }

    @PostConstruct
    public void initAccessToken(){

        if(accessToken == null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(clientIdName,clientIdValue);
            map.add(clientSecretName,clientSecretValue);
            map.add(clientGrantName,clientGrantValue);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            try {
                OAuthTokenResponse response = restTemplate.postForObject(getTokenUrl, request, OAuthTokenResponse.class);
                if(response!=null) {
                    accessToken = response.getAccessToken();
                    logger.info("get access token from OAuth Service {} ", accessToken);
                }
            } catch (Exception e) {
                logger.error("failed to fetch access token from OAuth Service {}", e.getMessage());
            }

        }
    }

    public String getAccessToken() { return accessToken; }

}
