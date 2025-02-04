package com.sun.wow

import com.sun.wow.dto.AuthTokenResponse
import com.sun.wow.service.BlizzardOauthService
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

}
