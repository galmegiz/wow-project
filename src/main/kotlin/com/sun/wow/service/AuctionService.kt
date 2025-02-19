package com.sun.wow.service

import com.sun.wow.client.AuctionClient
import com.sun.wow.repository.AuctionRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

@Service
class AuctionService(
    private val auctionRepository: AuctionRepository,
    private val auctionClient: AuctionClient
) {
    @Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    fun updateAuctionPeriodically() {
        runBlocking {
            val deferredResult = async {
                val commodityAuctionItem = auctionClient.getCommodityAuctionItems()
                auctionRepository.saveAll(commodityAuctionItem)
            }
            val deferredResult2 = async {
                val result = auctionClient.getAuctionItems()
                auctionRepository.saveAll(result)
            }
            deferredResult.await()
            deferredResult2.await()
        }
    }
}