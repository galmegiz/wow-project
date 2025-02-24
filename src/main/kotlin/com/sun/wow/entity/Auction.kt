package com.sun.wow.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.wow.client.dto.AuctionItemResponse
import com.sun.wow.constant.Copper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
@IdClass(AuctionId::class)
data class Auction(
    @Id
    val id: Long,
    @Id
    val lastModifiedTime: LocalDateTime,
    val itemId: Long,
    val quantity: Int, // 수량
    val timeLeft: String, // 경매 남은 시간
    val price: Copper,
) {
    @ManyToOne
    lateinit var item: Item
    companion object {
        val AUCTION_NOT_MODIFIED = emptyList<Auction>()
    }
}