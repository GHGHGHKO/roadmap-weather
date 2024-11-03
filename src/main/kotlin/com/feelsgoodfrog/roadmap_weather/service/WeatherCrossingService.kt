package com.feelsgoodfrog.roadmap_weather.service

import com.feelsgoodfrog.roadmap_weather.dto.WeatherCrossingApiResponseDto
import com.feelsgoodfrog.roadmap_weather.feign.WeatherCrossingClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class WeatherCrossingService(
    private val weatherCrossingClient: WeatherCrossingClient
) {

    @Value("\${roadmap-weather.api.visual-crossing.key}")
    private val key: String = ""

    fun getVisualCrossingWebJson(location: String): WeatherCrossingApiResponseDto? {
        val unitGroup = "metric"
        val contentType = "json"
        return weatherCrossingClient
            .getVisualCrossingWebServices(
                location, unitGroup, key, contentType
            )
    }
}
