package com.sun.wow.client

import URL_SUFFIX
import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.ItemResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
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
    fun getItemInfoById(itemId: Long): ItemResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }


        val fullUrl = itemUrl + itemId + URL_SUFFIX

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<ItemResponse> = restTemplate.exchange<ItemResponse>(fullUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw IllegalStateException()
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
            else -> throw IllegalStateException()
        }
    }
}