package com.sun.wow.client

import com.sun.wow.dto.AuctionHouseResponse
import com.sun.wow.dto.AuthTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class AuctionClient(
    private val restTemplate: RestTemplate,
    private val blizzardOauthClient: BlizzardOauthClient,
    @Value("\${blizzard.url.auction}")
    private val auctionUrl: String
) {

    fun getAuctionItems(): AuctionHouseResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<AuctionHouseResponse> = restTemplate.exchange<AuctionHouseResponse>(auctionUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw IllegalStateException()
        }
    }
}