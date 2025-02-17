package com.sun.wow.client

import com.sun.wow.dto.AuthTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class BlizzardOauthClient(
    @Value("\${blizzard.client-id}")
    private val clientId: String,
    @Value("\${blizzard.client-secret}")
    private val clientSecret: String,
    @Value("\${blizzard.url.access-token}")
    private val accessTokenUrl: String,
    private val restTemplate: RestTemplate,
) {
    private val auth: String = "$clientId:$clientSecret"
    private val encodedAuth: String = java.util.Base64.getEncoder().encodeToString(auth.toByteArray())
    private val authHeader: String = "Basic $encodedAuth"
    private val requestBody: String = "grant_type=client_credentials"

    private var cachedToken: AuthTokenResponse? = null
    private var cacheExpireTime: Long = 0

    /**
     * Access tokens last for 24 hours.
     */
    fun getToken(): AuthTokenResponse {
        val currentToken = cachedToken
        if(currentToken != null && System.currentTimeMillis() < cacheExpireTime) {
            return currentToken
        }

        val newToken = fetchNewToken()
        cachedToken = newToken
        cacheExpireTime = System.currentTimeMillis() + newToken.expiresIn
        return newToken
    }

    private fun fetchNewToken(): AuthTokenResponse {
        val headers: HttpHeaders = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
            it.set("Authorization", authHeader)
        }

        val request: HttpEntity<String> = HttpEntity(requestBody, headers)
        val response: ResponseEntity<AuthTokenResponse> = restTemplate.postForEntity(accessTokenUrl, request, AuthTokenResponse::class.java)

        val token = when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw IllegalStateException()
        }
        return token
    }
}