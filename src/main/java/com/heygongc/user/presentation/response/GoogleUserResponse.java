package com.heygongc.user.presentation.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserResponse (
    String iss,
    String azp,
    String aud,
    String sub,
    String email,
    @JsonProperty("email_verified")
    String emailVerified,
    @JsonProperty("at_hash")
    String atHash,
    String name,
    String picture,
    @JsonProperty("given_name")
    String givenName,
    @JsonProperty("family_name")
    String familyName,
    String locale,
    String iat,
    String exp,
    String alg,
    String kid,
    String typ,
    String scope
) {
}