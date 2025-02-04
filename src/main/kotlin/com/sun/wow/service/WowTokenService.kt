package com.sun.wow.service

import com.sun.wow.dto.AuthTokenResponse
import com.sun.wow.dto.WowTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class WowTokenService(
    private val restTemplate: RestTemplate,
    private val blizzardOauthService: BlizzardOauthService,
    @Value("\${blizzard.url.wow-token}")
    private val wowTokenUrl: String,
) {

    fun getCurrentTokenIndex(): WowTokenResponse {
        val token: AuthTokenResponse = blizzardOauthService.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<WowTokenResponse> = restTemplate.exchange<WowTokenResponse>(wowTokenUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw IllegalStateException()
        }
    }
}