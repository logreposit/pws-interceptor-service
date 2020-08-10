package com.logreposit.pwsinterceptorservice.mappers

import com.logreposit.pwsinterceptorservice.domain.PwsData
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object PwsDataMapper {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    fun toPwsData(params: Map<String, String>): PwsData {
        val action: String = urlDecode(checkNotNull(params["action"]) { "action has to be set" })
        val id: String = urlDecode(checkNotNull(params["ID"]) { "ID has to be set" })
        val password: String = urlDecode(checkNotNull(params["PASSWORD"]) { "PASSWORD has to be set" })

        // [YYYY-MM-DD HH:MM:SS (mysql format)] In Universal Coordinated Time (UTC) Not local time / or string "now"
        val dateUtc: String = urlDecode(checkNotNull(params["dateutc"]) { "dateutc has to be set" })

        val date = getInstant(date = dateUtc)

        return PwsData(
                action = action,
                id = id,
                password = password,
                dateUtc = date,
                realtime = params["realtime"]?.toInt(),
                realtimeFrequency = params["rtfreq"]?.toDouble(),
                weather = params["weather"]?.let { urlDecode(it) },
                clouds = params["clouds"]?.let { urlDecode(it) },
                softwareType = params["softwaretype"]?.let { urlDecode(it) },
                windDirection = params["winddir"]?.toDouble(),
                windSpeed = params["windspeedmph"]?.toDouble(),
                windGust = params["windgustmph"]?.toDouble(),
                windGustDirection = params["windgustdir"]?.toDouble(),
                windSpeedAvg2m = params["windspdmph_avg2m"]?.toDouble(),
                windGust10m = params["windgustmph_10m"]?.toDouble(),
                windGustDirection10m = params["windgustdir_10m"]?.toDouble(),
                humidity = params["humidity"]?.toDouble(),
                dewpoint = params["dewptf"]?.toDouble(),
                temperature = params["tempf"]?.toDouble(),
                temperature2 = params["temp2f"]?.toDouble(),
                temperature3 = params["temp3f"]?.toDouble(),
                temperature4 = params["temp4f"]?.toDouble(),
                rainfall = params["rainin"]?.toDouble(),
                rainfallDaily = params["dailyrainin"]?.toDouble(),
                barometricPressure = params["baromin"]?.toDouble(),
                soilTemperature = params["soiltempf"]?.toDouble(),
                soilTemperature2 = params["soiltemp2f"]?.toDouble(),
                soilTemperature3 = params["soiltemp3f"]?.toDouble(),
                soilTemperature4 = params["soiltemp4f"]?.toDouble(),
                soilMoisture = params["soilmoisture"]?.toDouble(),
                soilMoisture2 = params["soilmoisture2"]?.toDouble(),
                soilMoisture3 = params["soilmoisture3"]?.toDouble(),
                soilMoisture4 = params["soilmoisture4"]?.toDouble(),
                leafWetness = params["leafwetness"]?.toDouble(),
                leafWetness2 = params["leafwetness2"]?.toDouble(),
                solarRadiation = params["solarradiation"]?.toDouble(),
                uvIndex = params["UV"]?.toDouble(),
                visibility = params["visibility"]?.toDouble(),
                indoorTemperature = params["indoortempf"]?.toDouble(),
                indoorHumidity = params["indoorhumidity"]?.toDouble(),
                airQualityNo = params["AqNO"]?.toDouble(),
                airQualityNo2t = params["AqNO2T"]?.toDouble(),
                airQualityNo2 = params["AqNO2"]?.toDouble(),
                airQualityNo2y = params["AqNO2Y"]?.toDouble(),
                airQualityNox = params["AqNOX"]?.toDouble(),
                airQualityNoy = params["AqNOY"]?.toDouble(),
                airQualityNo3 = params["AqNO3"]?.toDouble(),
                airQualitySo4 = params["AqSO4"]?.toDouble(),
                airQualitySo2 = params["AqSO2"]?.toDouble(),
                airQualitySo2t = params["AqSO2T"]?.toDouble(),
                airQualityCo = params["AqCO"]?.toDouble(),
                airQualityCot = params["AqCOT"]?.toDouble(),
                airQualityEc = params["AqEC"]?.toDouble(),
                airQualityOc = params["AqOC"]?.toDouble(),
                airQualityBc = params["AqBC"]?.toDouble(),
                airQualityUvAeth = params["AqUV-AETH"]?.toDouble(),
                airQualityPm25 = params["AqPM2.5"]?.toDouble(),
                airQualityPm10 = params["AqPM10"]?.toDouble(),
                airQualityOzone = params["AqOZONE"]?.toDouble()
        )
    }

    private fun urlDecode(encoded: String?) = URLDecoder.decode(encoded, StandardCharsets.UTF_8)

    private inline fun <T : Any> checkNotNull(value: T?, lazyMessage: () -> Any): T {
        if (value == null) {
            val message = lazyMessage()
            throw PwsDataValidationException(message.toString())
        } else {
            return value
        }
    }

    private fun getInstant(date: String): Instant {
        if (date == "now") {
            return Instant.now()
        }

        return LocalDateTime.parse(date, dateTimeFormatter).atZone(ZoneId.of("UTC")).toInstant()
    }
}
