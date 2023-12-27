package com.heygongc.user.presentation.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleUserResponse (
    String iss,
    String azp,
    String aud,
    String sub,
    String email,
    String email_verified,
    String at_hash,
    String name,
    String picture,
    String given_name,
    String family_name,
    String locale,
    String iat,
    String exp,
    String alg,
    String kid,
    String typ,
    String scope
) {
}