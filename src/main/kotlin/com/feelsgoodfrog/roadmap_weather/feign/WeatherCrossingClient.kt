package com.feelsgoodfrog.roadmap_weather.feign

import com.feelsgoodfrog.roadmap_weather.dto.WeatherCrossingApiResponseDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient("weather", url = "\${roadmap-weather.api.visual-crossing.url}")
fun interface WeatherCrossingClient {

    @GetMapping("/VisualCrossingWebServices/rest/services/timeline/{location}")
    @Cacheable(cacheNames = ["weathers"], key = "#location")
    fun getVisualCrossingWebServices(
        @PathVariable location: String,
        @RequestParam unitGroup: String,
        @RequestParam key: String,
        @RequestParam contentType: String
    ): WeatherCrossingApiResponseDto?
}
