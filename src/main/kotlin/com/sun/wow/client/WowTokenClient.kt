package com.sun.wow.client

import com.sun.wow.dto.AuthTokenResponse
import com.sun.wow.dto.WowTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class WowTokenClient(
    private val restTemplate: RestTemplate,
    private val blizzardOauthClient: BlizzardOauthClient,
    @Value("\${blizzard.url.wow-token}")
    private val wowTokenUrl: String,
) {

    fun getCurrentTokenIndex(): WowTokenResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
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