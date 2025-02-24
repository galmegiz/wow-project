package com.sun.wow.entity

import java.io.Serializable
import java.time.LocalDateTime

data class AuctionId(
    var id: Long = 0,
    var lastModifiedTime: LocalDateTime = LocalDateTime.now()
) : Serializable