package com.logreposit.pwsinterceptorservice.services

import com.logreposit.pwsinterceptorservice.configurations.ApplicationConfiguration
import com.logreposit.pwsinterceptorservice.util.logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.temporal.ChronoUnit

@Service
class WeatherUndergroundService(
        restTemplateBuilder: RestTemplateBuilder,
        private val applicationConfiguration: ApplicationConfiguration
) {
    private val logger = logger()
    private val restTemplate: RestTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .build()

    companion object {
        const val WU_URL_STANDARD = "https://weatherstation.wunderground.com/weatherstation/updateweatherstation.php"
        const val WU_URL_RAPID_FIRE = "https://rtupdate.wunderground.com/weatherstation/updateweatherstation.php" // realtime=1
    }

    @Retryable(
            value = [RestClientException::class],
            maxAttempts = 5,
            backoff = Backoff(delay = 2000)
    )
    fun forward(params: Map<String, String>) {
        if (applicationConfiguration.weatherUndergroundForwardingEnabled != true) {
            logger.info("Forwarding Weather-Data to WeatherUnderground is disabled. ")
            return
        }

        params.ifEmpty { throw IllegalArgumentException("params must not be empty") }

        val wuEndpoint = getWeatherUndergroundEndpoint(params)
        val parameters = params.map { it.key + "=" + it.value }.joinToString(separator = "&")
        val url = "${wuEndpoint}?${parameters}"

        logger.info("Sending data to WeatherUnderground service: {}", url)

        val response = restTemplate.getForObject(url, String::class.java)

        logger.info("Response from WeatherUnderground service: {}", response)
    }

    @Recover
    fun recover(e: Throwable, params: Map<String, String>) {
        logger.error("Could not forward data to WeatherUnderground service: {}", params, e)

        throw e
    }

    private fun getWeatherUndergroundEndpoint(params: Map<String, String>): String {
        val realtime = params["realtime"]

        if (realtime == null || realtime != "1") {
            logger.debug("realtime was not set or not set to 1, upstream will be 'weatherstation.wunderground.com'")

            return WU_URL_STANDARD
        }

        logger.debug("realtime was set to 1, upstream will be 'rtupdate.wunderground.com'")

        return WU_URL_RAPID_FIRE
    }
}
