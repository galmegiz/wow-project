package com.sun.wow.client

import com.sun.wow.client.dto.AuctionHouseResponse
import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.CommodityAuctionHouseResponse
import com.sun.wow.entity.Auction
import com.sun.wow.exception.BlizzardApiException
import com.sun.wow.util.convertToLocalDateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import java.time.LocalDateTime

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

    fun getAuctionItems(): List<Auction> {
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
                val lastModifiedLocalDateTime = lastModifiedTime?.convertToLocalDateTime() ?: LocalDateTime.now()

                return body.auctions.map { it.toEntity(lastModifiedLocalDateTime) }
            }
            HttpStatus.NOT_MODIFIED -> {
                Auction.AUCTION_NOT_MODIFIED
            }
            else -> throw BlizzardApiException(response.statusCode)
        }
    }

    fun getCommodityAuctionItems(): List<Auction> {
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
                val lastModifiedLocalDateTime = lastModifiedTime?.convertToLocalDateTime() ?: LocalDateTime.now()
                return body.auctions.map { it.toEntity(lastModifiedLocalDateTime) }
            }
            HttpStatus.NOT_MODIFIED -> {
                Auction.AUCTION_NOT_MODIFIED
            }
            else -> throw BlizzardApiException(response.statusCode)
        }
    }
}