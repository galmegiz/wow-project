package com.sun.wow.client.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.sun.wow.constant.Copper

data class AuctionHouseResponse(
    val auctions: List<Auction>
) {
    companion object {
        val NOT_MODIFIED = AuctionHouseResponse(emptyList())
    }
}

data class CommodityAuctionHouseResponse(
    val auctions: List<CommodityAuction>
) {
    companion object {
        val NOT_MODIFIED = CommodityAuctionHouseResponse(emptyList())
    }
}

data class Auction(
    val id: Long,
    val item: AuctionItem,
    val buyout: Copper, // 즉시구매가, copper 기준
    val quantity: Int, // 수량
    @get:JsonProperty("time_left")
    val timeLeft: String // 경매 남은 시간
)

data class CommodityAuction(
    val id: Long,
    val item: CommodityAuctionItem,
    val quantity: Int,
    @get:JsonProperty("unit_price")
    val unitPrice: Copper,
    @get:JsonProperty("time_left")
    val timeLeft: String
)

sealed class AuctionItem{
    abstract val id: Long

    companion object {
        @JsonCreator
        @JvmStatic
        fun create(map: Map<String, Any>): AuctionItem {
            return if (map.containsKey("pet_species_id")) {
                PetAuctionItem(
                    id = (map["id"] as Number).toLong(),
                    modifiers = convertModifier(map["modifiers"] as? List<Map<String, Any>>),
                    petBreedId = map["pet_breed_id"] as Int,
                    petLevel = map["pet_level"] as Int,
                    petQualityId = map["pet_quality_id"] as Int,
                    petSpeciesId = map["pet_species_id"] as Int
                )
            } else {
                RegularAuctionItem(
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

data class RegularAuctionItem(
    override val id: Long,
    val modifiers: List<Modifier>,
    @JsonProperty("bonus_list")
    val bonusList: List<Int> = listOf(), // 아이템 보너스 id(소켓, 부가효과등)
): AuctionItem()

data class PetAuctionItem(
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
): AuctionItem()

data class CommodityAuctionItem(
    val id: Long
)

data class Modifier(
    val type: Int,
    val value: Int
)