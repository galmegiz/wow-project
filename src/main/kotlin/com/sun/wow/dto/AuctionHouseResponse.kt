package com.sun.wow.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper

data class AuctionHouseResponse(
    val auctions: List<Auction>
)

data class Auction(
    val id: Long,
    val item: AuctionItem,
    val buyout: Long, // 즉시구매가, copper 기준
    val quantity: Int, // 수량
    @JsonProperty("time_left")
    val timeLeft: String // 경매 남은 시간
)

/*@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, // Auto-detects correct subclass
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"

)
@JsonSubTypes(
    JsonSubTypes.Type(value = RegularAuctionItem::class, name = "regular"),
    JsonSubTypes.Type(value = PetAuctionItem::class, name = "pet")
)*/
sealed class AuctionItem{
    abstract val id: Long
    abstract val modifiers: List<Modifier>

    companion object {
        val objectMapper = ObjectMapper()
        @JsonCreator
        @JvmStatic
        fun create(map: Map<String, Any>): AuctionItem {
            return if (map.containsKey("pet_species_id")) {
                PetAuctionItem(
                    id = (map["id"] as Number).toLong(),
                    modifiers = (map["modifiers"] as? List<Map<String, Any>>)?.map {
                        Modifier(
                            type = (it["type"] as Number).toInt(),
                            value = (it["value"] as Number).toInt()
                        )
                    } ?: emptyList(), // Convert modifiers properly
                    petBreedId = map["pet_breed_id"] as Int,
                    petLevel = map["pet_level"] as Int,
                    petQualityId = map["pet_quality_id"] as Int,
                    petSpeciesId = map["pet_species_id"] as Int
                )
            } else {
                RegularAuctionItem(
                    id = (map["id"] as Number).toLong(),
                    modifiers = (map["modifiers"] as? List<Map<String, Any>>)?.map {
                        Modifier(
                            type = (it["type"] as Number).toInt(),
                            value = (it["value"] as Number).toInt()
                        )
                    } ?: emptyList(), // Convert modifiers properly
                    bonusList = (map["bonus_lists"] as? List<Int>)?.toList() ?: emptyList()
                )
            }
        }
    }
}

data class RegularAuctionItem(
    override val id: Long,
    override val modifiers: List<Modifier>,
    @JsonProperty("bonus_list")
    val bonusList: List<Int> = listOf(), // 아이템 보너스 id(소켓, 부가효과등)
): AuctionItem()

data class PetAuctionItem(
    override val id: Long,
    override val modifiers: List<Modifier>,
    @JsonProperty("pet_breed_id")
    val petBreedId: Int,
    @JsonProperty("pet_level")
    val petLevel: Int,
    @JsonProperty("pet_quality_id")
    val petQualityId: Int,
    @JsonProperty("pet_species_id")
    val petSpeciesId: Int,
): AuctionItem()

/*


sealed class AuctionItem(
    abstract val id: Long,
    val context: Int, // 아이템 습득 경로
    @JsonProperty("bonus_list")
    val bonusList: List<Int> = listOf(), // 아이템 보너스 id(소켓, 부가효과등)
    val modifiers: List<Modifier>,
    @JsonProperty("pet_breed_id")
    val petBreedId: Int,
    @JsonProperty("pet_level")
    val petLevel: Int,
    @JsonProperty("pet_quality_id")
    val petQualityId: Int,
    @JsonProperty("pet_species_id")
    val petSpeciesId: Int,
)

data class RegularAuctionItem()
*/

data class Modifier(
    val type: Int,
    val value: Int
)