package com.sun.wow.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class BlizzardOauthService(
    @Value("\${blizzard.client-id}")
    private val clientId: String,
    @Value("\${blizzard.client-secret}")
    private val clientSecret: String,
    @Value("\${blizzard.token-url}")
    private val tokenUrl: String
) {
    private val restTemplate = RestTemplate()
    private val auth: String = "$clientId:$clientSecret"
    private val encodedAuth: String = java.util.Base64.getEncoder().encodeToString(auth.toByteArray())
    private val authHeader: String = "Basic $encodedAuth"
    private val requestBody: String = "grant_type=client_credentials"

    fun getToken(): AuthTokenResponse {
        val headers: HttpHeaders = HttpHeaders().also {
            it.contentType = MediaType.APPLICATION_FORM_URLENCODED
            it.set("Authorization", authHeader)
        }

        val request: HttpEntity<String> = HttpEntity(requestBody, headers)
        val response: ResponseEntity<AuthTokenResponse> = restTemplate.postForEntity(tokenUrl, request, AuthTokenResponse::class.java)

        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw IllegalStateException()
        }
    }
}