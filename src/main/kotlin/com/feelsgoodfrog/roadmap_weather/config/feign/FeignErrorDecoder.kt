package com.feelsgoodfrog.roadmap_weather.config.feign

import com.feelsgoodfrog.roadmap_weather.exception.ClientUnknownException
import com.feelsgoodfrog.roadmap_weather.exception.InvalidParameterException
import feign.Response
import feign.codec.ErrorDecoder
import java.lang.Exception

class FeignErrorDecoder : ErrorDecoder {
    private val defaultDecoder: ErrorDecoder = ErrorDecoder.Default()
    override fun decode(p0: String?, p1: Response): Exception {
        val lastEndpoint: String = p1.request().requestTemplate().path().split("/").last()
        if (p1.status() == 400) {
            throw InvalidParameterException("Bad Request Parameter '${lastEndpoint}'")
        } else if (p1.status() in 500..504) {
            throw ClientUnknownException("${p1.status()} Server error '${lastEndpoint}'")
        }
        return defaultDecoder.decode(p0, p1)
    }
}
