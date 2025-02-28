package com.sun.wow

import com.sun.wow.client.*
import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.service.AuctionUpdateService
import com.sun.wow.service.ItemInfoService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class WowApplicationTests {

	@Autowired
	private lateinit var blizzardOauthClient: BlizzardOauthClient
	@Autowired
	private lateinit var wowTokenService: WowTokenClient
	@Autowired
	private lateinit var realmClient: RealmClient
	@Autowired
	private lateinit var auctionClient: AuctionClient
	@Autowired
	private lateinit var itemClient: ItemClient
	@Autowired
	private lateinit var itemInfoService: ItemInfoService
	@Autowired
	private lateinit var auctionUpdateService: AuctionUpdateService

	@Test
	fun contextLoads() {
		val token: AuthTokenResponse = blizzardOauthClient.getToken()
		println(token)
	}

	@Test
	fun tokenTest() {
		val wowToken = wowTokenService.getCurrentTokenIndex()
		val wowToken2 = wowTokenService.getCurrentTokenIndex()
		print(wowToken)
	}

	@Test
	fun realmTest() {
		val realm = realmClient.getAllRealm()
		print(realm)
	}

	@Test
	fun auctionTest() {
		val auctions = auctionClient.getAuctionItems()

	}

	@Test
	fun commodityAuctionTest() {
		val auctions = auctionClient.getCommodityAuctionItems()
		val auction2 = auctionClient.getCommodityAuctionItems()

	}

	@Test
	fun itemTest() {
		val auctions = itemClient.getItemInfoById(35)
		println(auctions)

	}

	@Test
	fun itemInfoTest() {
		auctionUpdateService.updateAuctionPeriodically()
		val auctions = itemInfoService.getItemLowestPrice(35)
		println(auctions)

	}

	@Test
	fun itemInfo2Test() {
		val result = itemClient.getItemInfoByName("지맥 잔류물")
		println(result)

	}
}
