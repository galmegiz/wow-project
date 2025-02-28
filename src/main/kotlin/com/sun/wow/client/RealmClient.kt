package com.sun.wow.client

import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.dto.Realm
import com.sun.wow.client.dto.WowRealmResponse
import com.sun.wow.exception.BlizzardApiException
import ko_KR
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Service
class RealmClient(
    private val restTemplate: RestTemplate,
    private val blizzardOauthClient: BlizzardOauthClient,
    @Value("\${blizzard.url.wow-realm}")
    private val realmUrl: String
) {

    fun getAllRealm(): WowRealmResponse {
        val token: AuthTokenResponse = blizzardOauthClient.getToken()
        val headers: HttpHeaders = HttpHeaders().also {
            it.set("Authorization", "Bearer ${token.accessToken}")
        }

        val entity: HttpEntity<String> = HttpEntity(headers)
        val response: ResponseEntity<WowRealmResponse> = restTemplate.exchange<WowRealmResponse>(realmUrl, HttpMethod.GET, entity)
        return when (response.statusCode) {
            HttpStatus.OK -> response.body ?: throw IllegalStateException()
            else -> throw BlizzardApiException(response.statusCode)
        }
    }

    fun getTargetRealmId(realmName: String, locale: String = ko_KR): Int {
        val realm: Realm = getAllRealm().realms.find { it.name[locale] == realmName }
            ?: throw IllegalArgumentException()
        return realm.id
    }

}