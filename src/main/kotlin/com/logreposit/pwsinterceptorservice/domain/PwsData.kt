package com.logreposit.pwsinterceptorservice.domain

import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.Reading
import java.time.Instant

data class PwsData(
        // IMPORTANT all fields must be url escaped

        // action
        // [action=updateraw]
        // always supply this parameter to indicate you are making a weather observation upload
        val action: String,

        // ID - required
        // [ID as registered by wunderground.com]
        val id: String,

        // PASSWORD - required
        // [Station Key registered with this PWS ID, case sensitive]
        val password: String,

        // dateutc - required
        // [YYYY-MM-DD HH:MM:SS (mysql format)] In Universal Coordinated Time (UTC) Not local time
        val dateUtc: Instant,

        // realtime
        // 1 (if present)
        val realtime: Int? = null,

        // rtfreq
        // [frequency of realtime updates] e.g. 2.5
        val realtimeFrequency: Double? = null,

        // winddir
        //  [0-360 instantaneous wind direction]
        val windDirection: Double? = null,

        // windspeedmph
        // [mph instantaneous wind speed]
        val windSpeed: Double? = null,

        // windgustmph
        // [mph current wind gust, using software specific time period]
        val windGust: Double? = null,

        // windgustdir
        // [0-360 using software specific time period]
        val windGustDirection: Double? = null,

        // windspdmph_avg2m
        // [mph 2 minute average wind speed mph]
        val windSpeedAvg2m: Double? = null,

        // windgustmph_10m
        // [mph past 10 minutes wind gust mph ]
        val windGust10m: Double? = null,

        // windgustdir_10m
        // [0-360 past 10 minutes wind gust direction]
        val windGustDirection10m: Double? = null,

        // humidity
        // [% outdoor humidity 0-100%]
        val humidity: Double? = null,

        // dewptf
        // [F outdoor dewpoint F]
        val dewpoint: Double? = null,

        // tempf, temp2f, temp3f, temp4f
        // [F outdoor temperature]
        val temperature: Double? = null,
        val temperature2: Double? = null,
        val temperature3: Double? = null,
        val temperature4: Double? = null,

        // rainin
        // [rain inches over the past hour)] -- the accumulated rainfall in the past 60 min
        val rainfall: Double? = null,

        // dailyrainin
        // [rain inches so far today in local time]
        val rainfallDaily: Double? = null,

        // baromin
        // [barometric pressure inches]
        val barometricPressure: Double? = null,

        // weather
        // [text] -- metar style (+RA)
        val weather: String? = null,

        // clouds
        // [text] -- SKC, FEW, SCT, BKN, OVC
        val clouds: String? = null,

        // soiltempf, soiltemp2f, soiltemp3f, soiltemp4f
        // [F soil temperature]
        val soilTemperature: Double? = null,
        val soilTemperature2: Double? = null,
        val soilTemperature3: Double? = null,
        val soilTemperature4: Double? = null,

        // soilmoisture, soilmoisture2, soilmoisture3, soilmoisture4
        // [%]
        val soilMoisture: Double? = null,
        val soilMoisture2: Double? = null,
        val soilMoisture3: Double? = null,
        val soilMoisture4: Double? = null,

        // leafwetness, leafwetness2
        // [%]
        val leafWetness: Double? = null,
        val leafWetness2: Double? = null,

        // solarradiation
        // [W/m^2]
        val solarRadiation: Double? = null,

        // UV
        // [index]
        val uvIndex: Double? = null,

        // visibility
        // [nm visibility]
        val visibility: Double? = null,

        // indoortempf
        // [F indoor temperature F]
        val indoorTemperature: Double? = null,

        // indoorhumidity
        // [% indoor humidity 0-100]
        val indoorHumidity: Double? = null,

        // AqNO
        // [ NO (nitric oxide) ppb ]
        val airQualityNo: Double? = null,

        // AqNO2T
        // (nitrogen dioxide), true measure ppb
        val airQualityNo2t: Double? = null,

        // AqNO2
        // NO2 computed, NOx-NO ppb
        val airQualityNo2: Double? = null,

        // AqNO2Y
        // NO2 computed, NOy-NO ppb
        val airQualityNo2y: Double? = null,

        // AqNOX
        // NOx (nitrogen oxides) - ppb
        val airQualityNox: Double? = null,

        // AqNOY
        // NOy (total reactive nitrogen) - ppb
        val airQualityNoy: Double? = null,

        // AqNO3
        // NO3 ion (nitrate, not adjusted for ammonium ion) UG/M3
        val airQualityNo3: Double? = null,

        // AqSO4
        // SO4 ion (sulfate, not adjusted for ammonium ion) UG/M3
        val airQualitySo4: Double? = null,

        // AqSO2
        // (sulfur dioxide), conventional ppb
        val airQualitySo2: Double? = null,

        // AqSO2T
        // trace levels ppb
        val airQualitySo2t: Double? = null,

        // AqCO
        // CO (carbon monoxide), conventional ppm
        val airQualityCo: Double? = null,

        // AqCOT
        // CO trace levels ppb
        val airQualityCot: Double? = null,

        // AqEC
        // EC (elemental carbon) – PM2.5 UG/M3
        val airQualityEc: Double? = null,

        // AqOC
        // OC (organic carbon, not adjusted for oxygen and hydrogen) – PM2.5 UG/M3
        val airQualityOc: Double? = null,

        // AqBC
        // BC (black carbon at 880 nm) UG/M3
        val airQualityBc: Double? = null,

        // AqUV-AETH
        // UV-AETH (second channel of Aethalometer at 370 nm) UG/M3
        val airQualityUvAeth: Double? = null,

        // AqPM2.5
        // PM2.5 mass - UG/M3
        val airQualityPm25: Double? = null,

        // AqPM10
        // PM10 mass - PM10 mass
        val airQualityPm10: Double? = null,

        // AqOZONE
        // Ozone - ppb
        val airQualityOzone: Double? = null,

        // softwaretype
        // [text] ie: WeatherLink, VWS, WeatherDisplay
        val softwareType: String? = null
) {
    fun toReadings(): List<Reading> {
        return emptyList()
    }
}
