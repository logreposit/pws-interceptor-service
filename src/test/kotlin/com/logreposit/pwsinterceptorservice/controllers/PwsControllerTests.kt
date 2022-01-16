package com.logreposit.pwsinterceptorservice.controllers

import com.logreposit.pwsinterceptorservice.configurations.PwsConfiguration
import com.logreposit.pwsinterceptorservice.services.WeatherUndergroundService
import com.logreposit.pwsinterceptorservice.services.logreposit.LogrepositApiService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@WebMvcTest(controllers = [PwsController::class])
class PwsControllerTests {

    private companion object {
        private val PARAMS = mapOf(
                "ID" to "IABCDE91",
                "PASSWORD" to "Ud2IJAA2",
                "action" to "updateraww",
                "realtime" to "1",
                "rtfreq" to "5",
                "dateutc" to "now",
                "baromin" to "27.22",
                "tempf" to "57.2",
                "humidity" to "73",
                "windspeedmph" to "0",
                "windgustmph" to "0",
                "winddir" to "11",
                "dewptf" to "48.5",
                "rainin" to "0",
                "dailyrainin" to "0",
                "UV" to "0"
        )
    }

    @Autowired
    private lateinit var controller: MockMvc

    @MockBean
    private lateinit var pwsConfiguration: PwsConfiguration

    @MockBean
    private lateinit var weatherUndergroundService: WeatherUndergroundService

    @MockBean
    private lateinit var logrepositApiService: LogrepositApiService

    @BeforeEach
    fun setUp() {
        given(pwsConfiguration.allowedIds).willReturn(listOf("IABCDE91"))
    }

    @Test
    fun `given valid data to updateweatherstation endpoint will forward it as-is to the weatherUndergroundService and return 200 OK`() {
        controller
                .perform(
                        get("/weatherstation/updateweatherstation.php")
                                .params(getParams())
                ).andExpect(status().isOk())

        verify(weatherUndergroundService).forward(eq(PARAMS))
    }

    @Test
    fun `given missing ID in params will return 400 bad request`() {
        givenParamIsMissingItWillReturn400BadRequest(
                param = "ID",
                expectedMessage = "ID has to be set"
        )
    }

    @Test
    fun `given missing PASSWORD in params will return 400 bad request`() {
        givenParamIsMissingItWillReturn400BadRequest(
                param = "PASSWORD",
                expectedMessage = "PASSWORD has to be set"
        )
    }

    @Test
    fun `given missing action in params will return 400 bad request`() {
        givenParamIsMissingItWillReturn400BadRequest(
                param = "action",
                expectedMessage = "action has to be set"
        )
    }

    @Test
    fun `given missing dateutc in params will return 400 bad request`() {
        givenParamIsMissingItWillReturn400BadRequest(
                param = "dateutc",
                expectedMessage = "dateutc has to be set"
        )
    }

    @Test
    fun `given device not listed as allowed will return forbidden`() {
        controller
                .perform(
                        get("/weatherstation/updateweatherstation.php")
                                .params(getParams().also { it.set("ID", "NOTALLOWED") })
                )
                .andExpect(status().isForbidden())
                .andExpect(content().string("Device with ID 'NOTALLOWED' is not allowed to submit data."))
                .andReturn()

        verify(weatherUndergroundService, never()).forward(anyOrNull())
    }

    private fun givenParamIsMissingItWillReturn400BadRequest(param: String, expectedMessage: String) {
        controller
                .perform(
                        get("/weatherstation/updateweatherstation.php")
                                .params(getParams().also { it.remove(param) })
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedMessage))
                .andReturn()

        verify(weatherUndergroundService, never()).forward(anyOrNull())
    }

    private fun getParams(): MultiValueMap<String, String> {
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()

        PARAMS.forEach { p -> map.add(p.key, p.value)}

        return map
    }
}
