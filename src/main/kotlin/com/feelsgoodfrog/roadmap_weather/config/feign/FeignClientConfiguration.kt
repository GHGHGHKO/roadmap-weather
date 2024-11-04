package com.feelsgoodfrog.roadmap_weather.config.feign

import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean

class FeignClientConfiguration {

    @Bean
    fun errorDecoder() : ErrorDecoder {
        return FeignErrorDecoder();
    }
}