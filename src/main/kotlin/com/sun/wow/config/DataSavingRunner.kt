package com.sun.wow.config

import com.sun.wow.service.ItemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataSavingRunner(private val itemService: ItemService): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        println("run start!!!!!!!")
        CoroutineScope(Dispatchers.IO).launch {
            itemService.saveAndUpdate()  // 백그라운드에서 실행
        }
    }
}