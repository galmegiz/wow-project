package com.sun.wow.service

import com.sun.wow.entity.WowToken
import com.sun.wow.repository.WowTokenRepository
import com.sun.wow.util.convertToMillis
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import java.time.LocalDateTime
import kotlin.test.Test

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Rollback(false)
class WowTokenServiceTest {
    @Autowired
    private lateinit var wowTokenRepository: WowTokenRepository

    @Autowired
    private lateinit var wowTokenService: WowTokenService

    @BeforeAll
    fun setup() {
        val now = LocalDateTime.now()
        val tokens = (1..20).map {
            WowToken(
                lastUpdateTimestamp = now.minusMinutes(it.toLong()).convertToMillis(),
                price = (1000..5000).random().toLong()
            )
        }
        wowTokenRepository.saveAll(tokens)
    }

    @Test
    @DisplayName("특정 기간 데이터 가져오기")
    fun `getTokenPriceBetween - 특정 기간 동안 데이터 가져오기`() {
        // Given
        val startDateTime = LocalDateTime.now().minusMinutes(15)
        val endDateTime = LocalDateTime.now()

        // When
        val result = wowTokenService.getTokenPriceBetween(startDateTime, endDateTime)

        // Then
        assert(result.isNotEmpty()) { "결과 리스트가 비어 있지 않아야 합니다." }
        assert(result.all { it.lastUpdateTimestamp in startDateTime.convertToMillis()..endDateTime.convertToMillis() }) {
            "모든 결과가 지정된 시간 범위 내에 있어야 합니다."
        }
    }

    @Test
    @DisplayName("최근 10개 데이터 가져오기")
    fun `getTokenPriceHistory - 최근 10개 데이터 가져오기`() {
        // Given
        val pageable = PageRequest.of(0, 10)

        // When
        val result = wowTokenService.getTokenPriceHistory(pageable)

        // Then
        assertEquals(10, result.size, "반환된 데이터 개수가 10개여야 합니다.")
        assert(result.zipWithNext { a, b -> a.lastUpdateTimestamp >= b.lastUpdateTimestamp }.all { it }) {
            "데이터가 최신순으로 정렬되어 있어야 합니다."
        }
    }
}