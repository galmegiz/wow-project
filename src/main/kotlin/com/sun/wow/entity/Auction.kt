package com.sun.wow.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.wow.client.dto.AuctionItemResponse
import com.sun.wow.constant.Copper
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Auction(
    @Id
    val id: Long,
    val itemId: Long,
    val quantity: Int, // 수량
    val timeLeft: String, // 경매 남은 시간
    val price: Copper,
    val lastModifiedTime: LocalDateTime) {
    companion object {
        val AUCTION_NOT_MODIFIED = emptyList<Auction>()
    }
}