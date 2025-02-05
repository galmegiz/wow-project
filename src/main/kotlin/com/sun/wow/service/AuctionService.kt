package com.sun.wow.service

import com.sun.wow.dto.AuctionHouseResponse
import com.sun.wow.dto.AuthTokenResponse
import com.sun.wow.dto.WowRealmResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class AuctionService(
    private val restTemplate: RestTemplate,
    private val blizzardOauthService: BlizzardOauthService,
    @Value("\${blizzard.url.auction}")
    private val auctionUrl: String
) {

    fun getAuctionItems(): AuctionHouseResponse {
        val token: AuthTokenResponse = blizzardOauthService.getToken()
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