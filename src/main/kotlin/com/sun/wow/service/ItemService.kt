package com.sun.wow.service

import com.sun.wow.client.ItemClient
import com.sun.wow.client.Log
import com.sun.wow.entity.Item
import com.sun.wow.exception.BlizzardApiException
import com.sun.wow.repository.ItemRepository
import com.sun.wow.util.readItemIdsFromFile
import io.github.resilience4j.retry.annotation.Retry
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime



@Service
class ItemService(
    private val itemClient: ItemClient,
    private val itemRepository: ItemRepository
): Log {
    private var lastChunkIndex = 125
    private val filePath = "C:\\temp\\MyCSV.txt"
    private val lastIndexFilePath = "C:\\temp\\lastIndex.txt"
    private lateinit var idList: List<Long>
    private lateinit var chunkedList: List<List<Long>>
    private val chunkSize = 100

    @PostConstruct
    fun afterConstruct() {
        idList = readItemIdsFromFile(filePath)
        chunkedList = idList.chunked(chunkSize)
    }


    @Retry(name = "defaultConfig")
    suspend fun saveAndUpdate() {
        try {
            withContext(Dispatchers.IO) {
                while (lastChunkIndex < chunkedList.size) {

                    val jobs = chunkedList[lastChunkIndex].map { id ->
                        async { saveItemInfo(id) }
                    }
                    jobs.awaitAll()
                    logger.info("save item info finished : {}", lastChunkIndex)
                    File(lastIndexFilePath).appendText("${LocalDateTime.now()} ${lastChunkIndex} \n")
                    lastChunkIndex++
                    delay(10 * 60 * 1000)
                }
            }

        } catch (e: Exception) {
            logger.error("api error!", e)
            File(lastIndexFilePath).appendText("${LocalDateTime.now()} fail while chunk ${lastChunkIndex}} \n")
            throw e
        }
    }

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