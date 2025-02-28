package com.sun.wow.client

import URL_SUFFIX
import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.ItemResponse
import com.sun.wow.exception.BlizzardApiException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import org.springframework.web.util.UriComponentsBuilder

@Service
class ItemClient(
    private val restTemplate: RestTemplate,
    private val blizzardOauthClient: BlizzardOauthClient,
    @Value("\${blizzard.url.item}")
    private val itemUrl: String,
    @Value("\${blizzard.url.item-by-name}")
    private val itemByNameUrl: String
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun getItemInfoById(itemId: Long): ItemResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }


        val fullUrl = itemUrl + itemId + URL_SUFFIX

        val entity: HttpEntity<String> = HttpEntity(headers)
        return runCatching {
            val response: ResponseEntity<ItemResponse> = restTemplate.exchange<ItemResponse>(fullUrl, HttpMethod.GET, entity)
            response.body ?: throw IllegalStateException()
        }.getOrElse {
            logger.error("Blizzard Api Error : {}", it)

            when (it) {
                is HttpClientErrorException.NotFound -> return ItemResponse.createUnknownItemResponse(itemId)
                else -> throw BlizzardApiException(HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    fun getItemInfoByName(itemName: String): ItemResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }

        val url = UriComponentsBuilder.fromUriString(itemByNameUrl)
            .buildAndExpand(itemName)
            .toUriString()

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<ItemResponse> = restTemplate.exchange<ItemResponse>(url, HttpMethod.GET, entity)

        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw BlizzardApiException(response.statusCode)
        }
    }
}