package com.sun.wow.entity

import com.sun.wow.constant.Copper
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "item")
data class Item(
    @Id
    @Column(name = "item_id")
    val id: Long,
    val name: String,
    @Column(name = "media_id")
    val mediaId: Long,
    val level: Int,
    @Column(name = "purchase_price")
    val purchasePrice: Copper,
    @Column(name = "sell_price")
    val sellPrice: Copper,
    @Column(name = "is_equippable")
    val isEquippable: Boolean,
    @Column(name = "is_stackable")
    val isStackable: Boolean,
    @Column(length = 255)
    val description: String,
    @Column(name = "item_class_id")
    val itemClassId: Long,
    @Column(name = "item_subclass_id")
    val itemSubClassId: Long,
    @Column(name = "inventory_type")
    val inventoryType: String
) {
}