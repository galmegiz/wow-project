package com.sun.wow.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class WowTokenResponse(
    @JsonProperty("last_updated_timestamp")
    val lastUpdateTimeStamp: Long,
    @JsonProperty("price")
    val price: Long
)