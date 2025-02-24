package com.sun.wow.service

import com.sun.wow.repository.AuctionRepository
import org.springframework.stereotype.Service

@Service
class ItemInfoService (
    private val auctionRepository: AuctionRepository,
    private val itemService: ItemService
){

    fun getItemLowestPrice(itemId: Long) {
        val item = itemService.getItemInfo(itemId)
        val auction = auctionRepository.findTopByItemIdOrderByPrice(itemId)
        auction.item = item

        print(auction)
        print(item)
    }


}