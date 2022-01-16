package com.logreposit.pwsinterceptorservice.services.logreposit

import com.logreposit.pwsinterceptorservice.configurations.ApplicationConfiguration
import com.logreposit.pwsinterceptorservice.configurations.RetryConfiguration
import com.logreposit.pwsinterceptorservice.domain.PwsData
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.web.client.HttpServerErrorException
import java.time.Instant

@RestClientTest(LogrepositApiService::class)
@Import(RetryConfiguration::class)
class LogrepositApiServiceRestClientTests {
    @MockBean
    private lateinit var applicationConfiguration: ApplicationConfiguration

    @Autowired
    private lateinit var client: LogrepositApiService

    @Autowired
    private lateinit var server: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        given(applicationConfiguration.logrepositApiBaseUrl).willReturn("https://api.logreposit.com")
        given(applicationConfiguration.logrepositDeviceToken).willReturn("TOKEN")
    }

    @Test
    fun `given valid data it should finish successfully`() {
        server.expect(ExpectedCount.once(), requestTo("https://api.logreposit.com/v2/ingress/data"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.readings").isArray)
                .andExpect(jsonPath("$.readings.length()").value(1))
                .andExpect(jsonPath("$.readings[0].date").isString)
                .andExpect(jsonPath("$.readings[0].tags").isArray)
                .andExpect(jsonPath("$.readings[0].tags[0]").isMap)
                .andExpect(jsonPath("$.readings[0].tags[0].name").value("device_id"))
                .andExpect(jsonPath("$.readings[0].tags[0].value").value("PERCH87"))
                .andExpect(jsonPath("$.readings[0].fields").isArray)
                .andExpect(jsonPath("$.readings[0].fields.length()").value(3))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"outdoor_temperature\")].value").value(32.61))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"outdoor_temperature\")].datatype").value("FLOAT"))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"humidity\")].value").value(42.0))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"humidity\")].datatype").value("FLOAT"))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"clouds\")].value").value("someText"))
                .andExpect(jsonPath("$.readings[0].fields[?(@.name == \"clouds\")].datatype").value("STRING"))
                .andRespond(MockRestResponseCreators.withSuccess())

        client.pushData(pwsData = samplePwsData())

        server.verify()
    }

    @Test
    fun `given server error when pushing data it should retry it 4 times (5 times total) before giving up`() {
        server.expect(ExpectedCount.times(5), requestTo("https://api.logreposit.com/v2/ingress/data"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withServerError())

        val started = System.currentTimeMillis()

        val thrown = assertThrows<HttpServerErrorException.InternalServerError> {
            client.pushData(pwsData = samplePwsData())
        }

        Assertions.assertThat(thrown.message).isEqualTo("500 Internal Server Error: [no body]")
        Assertions.assertThat(System.currentTimeMillis() - started).isBetween(2000, 3000)

        server.verify()
    }

    @Test
    fun `given client error entity unprocessable when pushing data it update the device ingress definition`() {
        server.expect(ExpectedCount.times(1), requestTo("https://api.logreposit.com/v2/ingress/data"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.UNPROCESSABLE_ENTITY))

        server.expect(ExpectedCount.times(1), requestTo("https://api.logreposit.com/v2/ingress/definition"))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(jsonPath("$.measurements").isArray)
                .andExpect(jsonPath("$.measurements.length()").value(1))
                .andExpect(jsonPath("$.measurements[0].name").value("data"))
                .andExpect(jsonPath("$.measurements[0].tags").isArray)
                .andExpect(jsonPath("$.measurements[0].tags.length()").value(1))
                .andExpect(jsonPath("$.measurements[0].tags[0]").value("device_id"))
                .andExpect(jsonPath("$.measurements[0].fields").isArray)
                .andExpect(jsonPath("$.measurements[0].fields.length()").value(52))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"outdoor_temperature\")].description").value("outdoor temperature [F]"))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"outdoor_temperature\")].datatype").value("FLOAT"))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"humidity\")].description").value("outdoor humidity (0-100) [%]"))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"humidity\")].datatype").value("FLOAT"))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"clouds\")].description").value("amount of cloud cover (SKC, FEW, SCT, BKN, OVC) [okta]"))
                .andExpect(jsonPath("$.measurements[0].fields[?(@.name == \"clouds\")].datatype").value("STRING"))
                .andRespond(MockRestResponseCreators.withSuccess())

        client.pushData(pwsData = samplePwsData())

        server.verify()
    }

    private fun samplePwsData() = PwsData(
            id = "PERCH87",
            password = "somePassword",
            action = "updateraww",
            dateUtc = Instant.now(),
            temperature = 32.61,
            humidity = 42.0,
            clouds = "someText"
    )
}
