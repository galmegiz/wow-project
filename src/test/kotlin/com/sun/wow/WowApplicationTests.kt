package com.sun.wow

import com.sun.wow.client.dto.AuthTokenResponse
import com.sun.wow.client.AuctionClient
import com.sun.wow.client.BlizzardOauthClient
import com.sun.wow.client.RealmClient
import com.sun.wow.client.WowTokenClient
import com.sun.wow.client.dto.CommodityAuctionHouseResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class WowApplicationTests {

	@Autowired
	private lateinit var blizzardOauthClient: BlizzardOauthClient
	@Autowired
	private lateinit var wowTokenService: WowTokenClient
	@Autowired
	private lateinit var realmClient: RealmClient
	@Autowired
	private lateinit var auctionClient: AuctionClient

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
}
