package com.logreposit.pwsinterceptorservice.mappers

import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test

class PwsDataMapperTests {

    companion object {
        private val baseParameters = mapOf(
                "ID" to "myId",
                "PASSWORD" to "myPassword",
                "action" to "updateraw",
                "dateutc" to "2000-01-01+10%3A32%3A35"
        )
    }

    @Test
    fun testToPwsData_givenUrlEncodedData_expectDecodeCorrectly() {
        val parameters = mapOf(
                "ID" to "myId",
                "PASSWORD" to "myPassword",
                "action" to "updateraw",
                "dateutc" to "2000-01-01+10%3A32%3A35"
        )

        val pwsData = PwsDataMapper.toPwsData(parameters)

        SoftAssertions().apply {
            assertThat(pwsData.id).isEqualTo("myId")
            assertThat(pwsData.password).isEqualTo("myPassword")
            assertThat(pwsData.action).isEqualTo("updateraw")
            assertThat(pwsData.dateUtc).isEqualTo("2000-01-01 10:32:35")
        }.assertAll()
    }

    @Test
    fun testToPwsData_givenDoublesAndIntegers_expectConvertedToDoubleCorrectly() {
        val parameters = baseParameters + mapOf(
                "tempf" to "68",
                "humidity" to "12.34563"
        )

        val pwsData = PwsDataMapper.toPwsData(parameters)

        SoftAssertions().apply {
            assertThat(pwsData.id).isEqualTo("myId")
            assertThat(pwsData.password).isEqualTo("myPassword")
            assertThat(pwsData.action).isEqualTo("updateraw")
            assertThat(pwsData.dateUtc).isEqualTo("2000-01-01 10:32:35")
            assertThat(pwsData.temperature).isEqualTo(68.0)
            assertThat(pwsData.humidity).isEqualTo(12.34563)
        }.assertAll()
    }
}
