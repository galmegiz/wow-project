package com.sun.wow.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.wow.constant.Copper
import com.sun.wow.entity.Auction
import java.time.LocalDateTime

data class AuctionHouseResponse(
    val auctions: List<AuctionResponse>
) {
    companion object {
        val NOT_MODIFIED = AuctionHouseResponse(emptyList())
    }

    fun toEntity(lastModifiedTime: LocalDateTime): List<Auction> {
        return this.auctions.map { it.toEntity(lastModifiedTime) }
    }
}

data class CommodityAuctionHouseResponse(
    val auctions: List<CommodityAuctionResponse>,
) {
    companion object {
        val NOT_MODIFIED = CommodityAuctionHouseResponse(emptyList())
    }

    fun toEntity(lastModifiedTime: LocalDateTime): List<Auction> {
        return this.auctions.map { it.toEntity(lastModifiedTime) }
    }
}

data class AuctionResponse(
    val id: Long,
    val item: AuctionItemResponse,
    val buyout: Copper, // 즉시구매가, copper 기준
    val quantity: Int, // 수량
    @get:JsonProperty("time_left")
    val timeLeft: String // 경매 남은 시간
) {
    fun toEntity(lastModifiedTime: LocalDateTime): Auction {
        return Auction(
            id = this.id,
            price = this.buyout,
            itemId = item.id,
            quantity = this.quantity,
            timeLeft = this.timeLeft,
            lastModifiedTime = lastModifiedTime,
        )
    }
}

data class CommodityAuctionResponse(
    val id: Long,
    val item: CommodityAuctionItem,
    val quantity: Int,
    @get:JsonProperty("unit_price")
    val unitPrice: Copper,
    @get:JsonProperty("time_left")
    val timeLeft: String
) {
    fun toEntity(lastModifiedTime: LocalDateTime): Auction {
        return Auction(
            id = this.id,
            price = this.unitPrice,
            itemId = item.id,
            quantity = this.quantity,
            timeLeft = this.timeLeft,
            lastModifiedTime = lastModifiedTime,
        )
    }
}

sealed class AuctionItemResponse{
    abstract val id: Long

    companion object {
        @JsonCreator
        @JvmStatic
        fun create(map: Map<String, Any>): AuctionItemResponse {
            return if (map.containsKey("pet_species_id")) {
                PetAuctionItemResponse(
                    id = (map["id"] as Number).toLong(),
                    modifiers = convertModifier(map["modifiers"] as? List<Map<String, Any>>),
                    petBreedId = map["pet_breed_id"] as Int,
                    petLevel = map["pet_level"] as Int,
                    petQualityId = map["pet_quality_id"] as Int,
                    petSpeciesId = map["pet_species_id"] as Int
                )
            } else {
                RegularAuctionItemResponse(
                    id = (map["id"] as Number).toLong(),
                    modifiers = convertModifier(map["modifiers"] as? List<Map<String, Any>>),
                    bonusList = (map["bonus_lists"] as? List<Int>)?.toList() ?: emptyList()
                )
            }
        }

        private fun convertModifier(maps: List<Map<String, Any>>?): List<Modifier> {
            return maps?.map { map ->
                Modifier(
                    type = map["type"] as Int,
                    value = map["value"] as Int
                )
            } ?: emptyList()
        }
    }
}

data class RegularAuctionItemResponse(
    override val id: Long,
    val modifiers: List<Modifier>,
    @JsonProperty("bonus_list")
    val bonusList: List<Int> = listOf(), // 아이템 보너스 id(소켓, 부가효과등)
): AuctionItemResponse()

data class PetAuctionItemResponse(
    override val id: Long,
    val modifiers: List<Modifier>,
    @JsonProperty("pet_breed_id")
    val petBreedId: Int,
    @JsonProperty("pet_level")
    val petLevel: Int,
    @JsonProperty("pet_quality_id")
    val petQualityId: Int,
    @JsonProperty("pet_species_id")
    val petSpeciesId: Int,
): AuctionItemResponse()

data class CommodityAuctionItem(
    val id: Long
)

data class Modifier(
    val type: Int,
    val value: Int
)