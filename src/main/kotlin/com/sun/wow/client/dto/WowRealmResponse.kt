package com.sun.wow.client.dto

data class WowRealmResponse(
    val realms: List<Realm>
)

data class Realm(
    val key: RealmKey,
    val name: Map<String, String>,
    val id: Int,
    val slug: String
)

data class RealmKey(
    val href: String
)