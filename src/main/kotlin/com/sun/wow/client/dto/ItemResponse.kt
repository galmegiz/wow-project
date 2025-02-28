package com.sun.wow.client.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.sun.wow.constant.Copper
import com.sun.wow.entity.Item

data class ItemResponse(
    val id: Long,
    val name: String,
    val level: Int,
    @get:JsonProperty("purchase_price")
    val purchasePrice: Copper,
    @get:JsonProperty("sell_price")
    val sellPrice: Copper,
    @get:JsonProperty("is_equippable")
    val isEquippable: Boolean,
    @get:JsonProperty("is_stackable")
    val isStackable: Boolean,
    @get:JsonSetter(nulls = Nulls.AS_EMPTY)
    val description: String,
    @get:JsonDeserialize(using = IdDeserializer::class)
    @get:JsonProperty("media")
    val mediaId: Long,
    @get:JsonDeserialize(using = TypeDeserializer::class)
    val quality: String,
    @get:JsonDeserialize(using = IdDeserializer::class)
    @get:JsonProperty("item_class")
    val itemClassId: Long,
    @get:JsonDeserialize(using = IdDeserializer::class)
    @get:JsonProperty("item_subclass")
    val itemSubClassId: Long,
    @get:JsonDeserialize(using = TypeDeserializer::class)
    @get:JsonProperty("inventory_type")
    val inventoryType: String
) {
    companion object {
        fun createUnknownItemResponse(itemId: Long): ItemResponse {
            return ItemResponse(
                itemId,
                name = "UNKNOWN",
                level = -1,
                purchasePrice = Copper(0),
                sellPrice = Copper(0),
                isEquippable = false,
                isStackable = false,
                description = "",
                mediaId = -1,
                quality = "UNKNOWN",
                itemClassId = -1,
                itemSubClassId = -1,
                inventoryType = "UNKNOWN"
            )
        }
    }

    fun toEntity(): Item {
        return Item(
            id = this.id,
            name = this.name,
            mediaId = this.mediaId,
            level = this.level,
            purchasePrice = this.purchasePrice,
            sellPrice = this.sellPrice,
            isEquippable = this.isEquippable,
            isStackable = this.isStackable,
            description = this.description ?: "",
            itemClassId = this.itemClassId,
            itemSubClassId = this.itemSubClassId,
            inventoryType = this.inventoryType
        )
    }
}

data class ItemByNameResponse(
    val page: Int,
    val pageSize: Int,
    val maxPageSize: Int,
    val pageCount: Int,
    val results: List<ItemResponse>,
) {
    fun toEntity(): List<Item> {
        return results.map {
            it.toEntity()
        }
    }
}

data class Media(val id: Long)

class IdDeserializer : JsonDeserializer<Long>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Long {
        val node: JsonNode = p.codec.readTree(p)
        return node.get("id").asLong()
    }
}

class TypeDeserializer : JsonDeserializer<String>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): String {
        val node: JsonNode = p.codec.readTree(p)
        return node.get("type").asText()
    }
}