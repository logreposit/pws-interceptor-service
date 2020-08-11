package com.logreposit.pwsinterceptorservice.controllers

import com.logreposit.pwsinterceptorservice.configurations.PwsConfiguration
import com.logreposit.pwsinterceptorservice.exceptions.DeviceNotAllowedException
import com.logreposit.pwsinterceptorservice.mappers.PwsDataMapper
import com.logreposit.pwsinterceptorservice.services.WeatherUndergroundService
import com.logreposit.pwsinterceptorservice.services.logreposit.LogrepositApiService
import com.logreposit.pwsinterceptorservice.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class PwsController(
        private val pwsConfiguration: PwsConfiguration,
        private val logrepositApiService: LogrepositApiService,
        private val weatherUndergroundService: WeatherUndergroundService
) {
    private val logger = logger()

    @GetMapping(value = ["/weatherstation/updateweatherstation.php"])
    fun weatherStation(@RequestParam params: Map<String, String>): ResponseEntity<String> {
        val pwsData = PwsDataMapper.toPwsData(params)

        authenticate(deviceId = pwsData.id)

        logrepositApiService.pushData(pwsData)
        weatherUndergroundService.forward(params = params)

        return ResponseEntity<String>("success", HttpStatus.OK)
    }

    private fun authenticate(deviceId: String) {
        if (pwsConfiguration.allowedIds.none {it == deviceId}) {
            logger.info("id {} is not allowed: {}", deviceId, pwsConfiguration.allowedIds)

            throw DeviceNotAllowedException("Device with ID '$deviceId' is not allowed to submit data.")
        }
    }
}
