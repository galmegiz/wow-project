package com.sun.wow

import com.sun.wow.service.AuthTokenResponse
import com.sun.wow.service.BlizzardOauthService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WowApplicationTests {

	@Autowired
	private lateinit var blizzardOauthService: BlizzardOauthService

	@Test
	fun contextLoads() {
		val token: AuthTokenResponse = blizzardOauthService.getToken()
		println(token)
	}

}
