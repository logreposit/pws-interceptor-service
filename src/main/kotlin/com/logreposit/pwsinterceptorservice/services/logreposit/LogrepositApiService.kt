package com.logreposit.pwsinterceptorservice.services.logreposit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.logreposit.pwsinterceptorservice.configurations.ApplicationConfiguration
import com.logreposit.pwsinterceptorservice.domain.PwsData
import com.logreposit.pwsinterceptorservice.mappers.LogrepositIngressDataMapper
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.IngressDefinition
import com.logreposit.pwsinterceptorservice.util.logger
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.time.temporal.ChronoUnit

@Service
class LogrepositApiService(
        restTemplateBuilder: RestTemplateBuilder,
        private val applicationConfiguration: ApplicationConfiguration
) {
    private val logger = logger()
    private val deviceDefinition = getDefinition()

    private val restTemplate: RestTemplate = restTemplateBuilder
            .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .build()

    @Retryable(
            value = [RestClientException::class],
            exclude = [HttpClientErrorException.UnprocessableEntity::class],
            maxAttempts = 5,
            backoff = Backoff(delay = 500)
    )
    fun pushData(pwsData: PwsData) {
        val data = LogrepositIngressDataMapper.toLogrepositIngressDto(pwsData = pwsData)
        val url = applicationConfiguration.logrepositApiBaseUrl + "/v2/ingress/data"

        logger.info("Sending data to Logreposit API ({}): {}", url, data)

        val response = restTemplate.postForObject(url, HttpEntity(data, createHeaders(applicationConfiguration.logrepositDeviceToken)), String::class.java)

        logger.info("Response from Logreposit API: {}", response)
    }

    @Recover
    fun recoverUnprocessableEntity(e: HttpClientErrorException.UnprocessableEntity, pwsData: PwsData) {
        logger.warn("Error while forwarding data to Logreposit API. Got unprocessable entity. Most likely a device definition validation error.", pwsData, e)
        logger.warn("Updating device ingress definition ...")

        val url = applicationConfiguration.logrepositApiBaseUrl + "/v2/ingress/definition"

        restTemplate.put(url, HttpEntity(deviceDefinition, createHeaders(applicationConfiguration.logrepositDeviceToken)))
    }

    @Recover
    fun recoverThrowable(e: Throwable, pwsData: PwsData) {
        logger.error("Could not forward data to Logreposit API: {}", pwsData, e)

        throw e
    }

    private fun getDefinition(): IngressDefinition {
        val yamlMapper = ObjectMapper(YAMLFactory())

        yamlMapper.registerModule(KotlinModule())

        val definitionAsString = LogrepositApiService::class.java.getResource("/device-definition.yaml").readText()

        return yamlMapper.readValue(definitionAsString, IngressDefinition::class.java)
    }

    private fun createHeaders(deviceToken: String?): HttpHeaders {
        val httpHeaders = HttpHeaders()

        httpHeaders["x-device-token"] = deviceToken

        return httpHeaders
    }
}
