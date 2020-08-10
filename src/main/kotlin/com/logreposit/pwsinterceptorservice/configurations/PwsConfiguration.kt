package com.logreposit.pwsinterceptorservice.configurations

import com.logreposit.pwsinterceptorservice.util.logger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

@Configuration
@ConfigurationProperties(prefix = "pws")
class PwsConfiguration {
    private val logger = logger()

    lateinit var allowedIds: List<String>

    @EventListener
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        if (allowedIds.isEmpty() || allowedIds.all { it.isBlank() }) {
            logger.warn("There are no Station IDs configured. Will not capture any data.")
        } else {
            logger.info("Data of the following Station IDs will be captured: {}", allowedIds)
        }
    }
}
