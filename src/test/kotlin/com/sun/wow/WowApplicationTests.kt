package com.sun.wow

import com.sun.wow.dto.AuthTokenResponse
import com.sun.wow.service.AuctionService
import com.sun.wow.service.BlizzardOauthService
import com.sun.wow.service.RealmService
import com.sun.wow.service.WowTokenService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WowApplicationTests {

	@Autowired
	private lateinit var blizzardOauthService: BlizzardOauthService
	@Autowired
	private lateinit var wowTokenService: WowTokenService
	@Autowired
	private lateinit var realmService: RealmService
	@Autowired
	private lateinit var auctionService: AuctionService

	@Test
	fun contextLoads() {
		val token: AuthTokenResponse = blizzardOauthService.getToken()
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
		val realm = realmService.getAllRealm()
		print(realm)
	}

	@Test
	fun auctionTest() {
		val auctions = auctionService.getAuctionItems()
		println(auctions.auctions.first())
	}

}
