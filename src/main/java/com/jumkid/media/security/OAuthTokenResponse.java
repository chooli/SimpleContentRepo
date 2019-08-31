package com.jumkid.media.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthTokenResponse {

    private String accessToken;

    private String tokenType;

    private Integer expiresIn;

    private String scope;

    private String clientId;

    private String error;

    private String errorDescription;

    public String getClientId() { return clientId; }

    @JsonProperty("client_id")
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getAccessToken() { return accessToken; }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getTokenType() { return tokenType; }

    @JsonProperty("token_type")
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) { this.scope = scope; }

    public String getError() {
        return error;
    }

    public void setError(String error) { this.error = error; }

    public String getErrorDescription() { return errorDescription; }

    @JsonProperty("error_description")
    public void setErrorDescription(String errorDescription) { this.errorDescription = errorDescription; }

}
