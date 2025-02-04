package com.sun.wow.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class WowTokenResponse(
    @JsonProperty("last_updated_timestamp")
    private val lastUpdateTimeStamp: Long,
    @JsonProperty("price")
    private val price: Long
)