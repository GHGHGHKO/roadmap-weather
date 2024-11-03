package com.feelsgoodfrog.roadmap_weather.controller

import com.feelsgoodfrog.roadmap_weather.dto.WeatherCrossingApiResponseDto
import com.feelsgoodfrog.roadmap_weather.service.WeatherCrossingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/weathers")
@RestController
class WeatherCrossingController(
    private val weatherCrossingService: WeatherCrossingService
) {

    @GetMapping("/{location}")
    fun get(
        @PathVariable location: String
    ): ResponseEntity<WeatherCrossingApiResponseDto> {
        return ResponseEntity.ok()
            .body(weatherCrossingService
                .getVisualCrossingWebJson(location))
    }
}
