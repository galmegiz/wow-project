package com.sun.wow.service

import com.sun.wow.client.ItemClient
import com.sun.wow.entity.Item
import com.sun.wow.repository.ItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ItemService(
    private val itemClient: ItemClient,
    private val itemRepository: ItemRepository
) {
    fun saveItemInfo(itemId: Long) {
        if (!itemRepository.existsById(itemId)) {
            val itemResponse = itemClient.getItemInfoById(itemId)
            itemRepository.save(itemResponse.toEntity())
        }
    }

    fun getItemInfo(itemId: Long): Item {
        return itemRepository.findByIdOrNull(itemId)
            ?: itemClient.getItemInfoById(itemId)
                .toEntity()
                .also { itemRepository.save(it) }
    }
}