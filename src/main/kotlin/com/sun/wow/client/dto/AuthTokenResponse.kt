package com.sun.wow.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
    @JsonProperty("sub")
    val sub: String
)
