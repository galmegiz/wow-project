package com.sun.wow.client

import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.WowTokenResponse
import com.sun.wow.entity.WowToken
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

    fun getCurrentTokenIndex(): WowToken {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<WowTokenResponse> = restTemplate.exchange<WowTokenResponse>(wowTokenUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> {
                val resBody = response.body ?: throw IllegalArgumentException()
                WowToken(resBody.lastUpdateTimeStamp, resBody.price)
            }

            else -> throw IllegalStateException()
        }
    }
}