package com.logreposit.pwsinterceptorservice.services

import com.logreposit.pwsinterceptorservice.configurations.ApplicationConfiguration
import com.logreposit.pwsinterceptorservice.configurations.RetryConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withServerError
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.HttpServerErrorException


@RunWith(SpringRunner::class)
@RestClientTest(WeatherUndergroundService::class)
@Import(RetryConfiguration::class)
class WeatherUndergroundServiceRestClientTests {

    @MockBean
    private lateinit var applicationConfiguration: ApplicationConfiguration

    @Autowired
    private lateinit var client: WeatherUndergroundService

    @Autowired
    private lateinit var server: MockRestServiceServer

    companion object {
        private val PARAMS = mapOf("ID" to "WUWSID12", "PASSWORD" to "abcdefg", "temp" to "12.345")
    }

    @Before
    fun setUp() {
        given(applicationConfiguration.weatherUndergroundForwardingEnabled).willReturn(true)
    }

    @Test
    fun `given empty params it should throw an IllegalArgumentException`() {
        val thrown = assertThrows<IllegalArgumentException> {
            client.forward(emptyMap())
        }

        assertThat(thrown.message).isEqualTo("params must not be empty")
    }

    @Test
    fun `given valid data it should finish successfully`() {
        server.expect(
                ExpectedCount.once(),
                requestTo("https://weatherstation.wunderground.com/weatherstation/updateweatherstation.php?ID=WUWSID12&PASSWORD=abcdefg&temp=12.345")
        ).andRespond(withSuccess())

        client.forward(params = PARAMS)

        server.verify()
    }

    @Test
    fun `given server error when forwarding weather data it should retry it 4 times (5 times total) before giving up`() {
        server.expect(
                ExpectedCount.times(5),
                requestTo("https://weatherstation.wunderground.com/weatherstation/updateweatherstation.php?ID=WUWSID12&PASSWORD=abcdefg&temp=12.345")
        ).andRespond(withServerError())

        val started = System.currentTimeMillis()

        val thrown = assertThrows<HttpServerErrorException.InternalServerError> {
            client.forward(params = PARAMS)
        }

        assertThat(thrown.message).isEqualTo("500 Internal Server Error: [no body]")
        assertThat(System.currentTimeMillis() - started).isBetween(8000, 8200)

        server.verify()
    }

    @Test
    fun `given valid data and realtime=1 it should choose rapidfire endpoint and finish successfully`() {
        server.expect(
                ExpectedCount.once(),
                requestTo("https://rtupdate.wunderground.com/weatherstation/updateweatherstation.php?ID=WUWSID12&PASSWORD=abcdefg&temp=12.345&realtime=1")
        ).andRespond(withSuccess())

        client.forward(params = PARAMS + ("realtime" to "1"))

        server.verify()
    }
}
