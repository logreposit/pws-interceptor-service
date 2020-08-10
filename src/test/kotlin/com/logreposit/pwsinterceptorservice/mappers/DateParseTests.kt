package com.logreposit.pwsinterceptorservice.mappers

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DateParseTests {

    @Test
    fun `test something`() {

        val mysqlDateString = "2020-08-07 15:31:00"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val parsedDate = dateFormat.parse(mysqlDateString)

        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //val parsedZonedDate = ZonedDateTime.parse(mysqlDateString, dateTimeFormatter)
        val parsedLocalDate = LocalDateTime.parse(mysqlDateString, dateTimeFormatter).atZone(ZoneId.of("UTC"))

        assertThat(parsedDate).isNotNull()

        val instantOfDate = parsedDate.toInstant()
        //val instantOfParsedZonedDate = parsedZonedDate.toInstant()
        val instantOfLocalDate = parsedLocalDate.toInstant()

        assertThat(instantOfDate).isNotNull()
        //assertThat(instantOfParsedZonedDate).isNotNull()
        assertThat(instantOfLocalDate).isNotNull()

        val instantString = instantOfDate.toString()
        //val instantOfParsedZonedDateString = instantOfParsedZonedDate.toString()
        val instantOfParsedLocalDateString = instantOfLocalDate.toString()

        assertThat(instantString).isNotBlank()
        //assertThat(instantOfParsedZonedDateString).isNotBlank()
        assertThat(instantOfParsedLocalDateString).isNotBlank()

    }
}
