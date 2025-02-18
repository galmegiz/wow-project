package com.sun.wow.client

import com.sun.wow.client.dto.AuctionHouseResponse
import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.CommodityAuctionHouseResponse
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
    private val auctionUrl: String,
    @Value("\${blizzard.url.commodity-auction}")
    private val commodityAuctionUrl: String
) {
    private var commodityLastModified: String? = null
    private var auctionLastModified: String? = null

    fun getAuctionItems(): AuctionHouseResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also { header ->
            header.set("Authorization", "Bearer ${token.accessToken}")
            auctionLastModified?.let{ header.set("If-Modified-Since", it) }
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<AuctionHouseResponse> =
            restTemplate.exchange<AuctionHouseResponse>(auctionUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> {
                val body: AuctionHouseResponse = response.body ?: throw IllegalStateException()
                val lastModifiedTime = response.headers["Last-Modified"]?.firstOrNull()
                if (lastModifiedTime != null) {
                    this.auctionLastModified = lastModifiedTime
                }
                return body
            }
            HttpStatus.NOT_MODIFIED -> {
                AuctionHouseResponse.NOT_MODIFIED
            }
            else -> throw IllegalStateException()
        }
    }

    fun getCommodityAuctionItems(): CommodityAuctionHouseResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also { header ->
            header.set("Authorization", "Bearer ${token.accessToken}")
            commodityLastModified?.let{ header.set("If-Modified-Since", it) }
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<CommodityAuctionHouseResponse> =
            restTemplate.exchange<CommodityAuctionHouseResponse>(commodityAuctionUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> {
                val body: CommodityAuctionHouseResponse = response.body ?: throw IllegalStateException()
                val lastModifiedTime = response.headers["Last-Modified"]?.firstOrNull()
                if (lastModifiedTime != null) {
                    this.commodityLastModified = lastModifiedTime
                }
                return body
            }
            HttpStatus.NOT_MODIFIED -> {
                CommodityAuctionHouseResponse.NOT_MODIFIED
            }
            else -> throw IllegalStateException()
        }
    }
}