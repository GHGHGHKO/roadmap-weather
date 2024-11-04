package com.feelsgoodfrog.roadmap_weather.config.exception

import com.feelsgoodfrog.roadmap_weather.exception.InvalidParameterException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalException {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(InvalidParameterException::class)
    fun invalidParameterException(
        e: InvalidParameterException
    ): ResponseEntity<ErrorResponse> {
        logger.info { "Bad Request InvalidParameterException message: ${e.message}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ErrorResponse.builder(
                    e,
                    HttpStatus.BAD_REQUEST,
                    e.message.toString())
                .build()
            )
    }
}
