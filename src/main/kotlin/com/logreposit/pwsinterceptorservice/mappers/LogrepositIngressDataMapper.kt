package com.logreposit.pwsinterceptorservice.mappers

import com.logreposit.pwsinterceptorservice.domain.PwsData
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.FloatField
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.IngressData
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.Reading
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.StringField
import com.logreposit.pwsinterceptorservice.services.logreposit.dtos.ingress.Tag

object LogrepositIngressDataMapper {
    fun toLogrepositIngressDto(pwsData: PwsData): IngressData = IngressData(
            readings = listOf(
                    Reading(
                            date = pwsData.dateUtc,
                            measurement = "data",
                            tags = listOf(Tag(name = "device_id", value = pwsData.id)),
                            fields = listOfNotNull(
                                    pwsData.windDirection?.let {
                                        FloatField(name = "wind_direction", value = it)
                                    },
                                    pwsData.windSpeed?.let {
                                        FloatField(name = "wind_speed", value = it)
                                    },
                                    pwsData.windGust?.let {
                                        FloatField(name = "wind_gust", value = it)
                                    },
                                    pwsData.windGustDirection?.let {
                                        FloatField(name = "wind_gust_direction", value = it)
                                    },
                                    pwsData.windSpeedAvg2m?.let {
                                        FloatField(name = "wind_speed_avg_2m", value = it)
                                    },
                                    pwsData.windGust10m?.let {
                                        FloatField(name = "wind_gust_10m", value = it)
                                    },
                                    pwsData.windGustDirection10m?.let {
                                        FloatField(name = "wind_gust_direction_10m", value = it)
                                    },
                                    pwsData.humidity?.let {
                                        FloatField(name = "humidity", value = it)
                                    },
                                    pwsData.dewpoint?.let {
                                        FloatField(name = "dew_point", value = it)
                                    },
                                    pwsData.temperature?.let {
                                        FloatField(name = "outdoor_temperature", value = it)
                                    },
                                    pwsData.temperature2?.let {
                                        FloatField(name = "temperature_2", value = it)
                                    },
                                    pwsData.temperature3?.let {
                                        FloatField(name = "temperature_3", value = it)
                                    },
                                    pwsData.temperature4?.let {
                                        FloatField(name = "temperature_4", value = it)
                                    },
                                    pwsData.rainfall?.let {
                                        FloatField(name = "rainfall_hourly", value = it)
                                    },
                                    pwsData.rainfallDaily?.let {
                                        FloatField(name = "rainfall_daily", value = it)
                                    },
                                    pwsData.barometricPressure?.let {
                                        FloatField(name = "barometric_pressure", value = it)
                                    },
                                    pwsData.weather?.let {
                                        StringField(name = "weather", value = it)
                                    },
                                    pwsData.clouds?.let {
                                        StringField(name = "clouds", value = it)
                                    },
                                    pwsData.soilTemperature?.let {
                                        FloatField(name = "soil_temperature", value = it)
                                    },
                                    pwsData.soilTemperature2?.let {
                                        FloatField(name = "soil_temperature_2", value = it)
                                    },
                                    pwsData.soilTemperature3?.let {
                                        FloatField(name = "soil_temperature_3", value = it)
                                    },
                                    pwsData.soilTemperature4?.let {
                                        FloatField(name = "soil_temperature_4", value = it)
                                    },
                                    pwsData.soilMoisture?.let {
                                        FloatField(name = "soil_moisture", value = it)
                                    },
                                    pwsData.soilMoisture2?.let {
                                        FloatField(name = "soil_moisture_2", value = it)
                                    },
                                    pwsData.soilMoisture3?.let {
                                        FloatField(name = "soil_moisture_3", value = it)
                                    },
                                    pwsData.soilMoisture4?.let {
                                        FloatField(name = "soil_moisture_4", value = it)
                                    },
                                    pwsData.leafWetness?.let {
                                        FloatField(name = "leaf_wetness", value = it)
                                    },
                                    pwsData.leafWetness2?.let {
                                        FloatField(name = "leaf_wetness_2", value = it)
                                    },
                                    pwsData.solarRadiation?.let {
                                        FloatField(name = "solar_radiation", value = it)
                                    },
                                    pwsData.uvIndex?.let {
                                        FloatField(name = "uv_index", value = it)
                                    },
                                    pwsData.visibility?.let {
                                        FloatField(name = "visibility", value = it)
                                    },
                                    pwsData.indoorTemperature?.let {
                                        FloatField(name = "indoor_temperature", value = it)
                                    },
                                    pwsData.indoorHumidity?.let {
                                        FloatField(name = "indoor_humidity", value = it)
                                    },
                                    pwsData.airQualityNo?.let {
                                        FloatField(name = "aq_no", value = it)
                                    },
                                    pwsData.airQualityNo2t?.let {
                                        FloatField(name = "aq_no2t", value = it)
                                    },
                                    pwsData.airQualityNo2?.let {
                                        FloatField(name = "aq_no2", value = it)
                                    },
                                    pwsData.airQualityNo2y?.let {
                                        FloatField(name = "aq_no2y", value = it)
                                    },
                                    pwsData.airQualityNox?.let {
                                        FloatField(name = "aq_nox", value = it)
                                    },
                                    pwsData.airQualityNoy?.let {
                                        FloatField(name = "aq_noy", value = it)
                                    },
                                    pwsData.airQualityNo3?.let {
                                        FloatField(name = "aq_no3", value = it)
                                    },
                                    pwsData.airQualitySo4?.let {
                                        FloatField(name = "aq_so4", value = it)
                                    },
                                    pwsData.airQualitySo2?.let {
                                        FloatField(name = "aq_so2", value = it)
                                    },
                                    pwsData.airQualitySo2t?.let {
                                        FloatField(name = "aq_so2t", value = it)
                                    },
                                    pwsData.airQualityCo?.let {
                                        FloatField(name = "aq_co", value = it)
                                    },
                                    pwsData.airQualityCot?.let {
                                        FloatField(name = "aq_cot", value = it)
                                    },
                                    pwsData.airQualityEc?.let {
                                        FloatField(name = "aq_ec", value = it)
                                    },
                                    pwsData.airQualityOc?.let {
                                        FloatField(name = "aq_oc", value = it)
                                    },
                                    pwsData.airQualityBc?.let {
                                        FloatField(name = "aq_bc", value = it)
                                    },
                                    pwsData.airQualityUvAeth?.let {
                                        FloatField(name = "aq_uv_aeth", value = it)
                                    },
                                    pwsData.airQualityPm25?.let {
                                        FloatField(name = "aq_pm_2_5", value = it)
                                    },
                                    pwsData.airQualityPm10?.let {
                                        FloatField(name = "aq_pm_10", value = it)
                                    },
                                    pwsData.airQualityOzone?.let {
                                        FloatField(name = "aq_ozone", value = it)
                                    }
                            )
                    )
            )
    )
}
