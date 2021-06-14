package com.anhvt.weathersearch.domain.entity

import java.time.LocalDate

data class WeatherDetails(
    val date: LocalDate,
    val averageTemperature: Float,
    val pressure: Int,
    val humidity: Float,
    val description: String
)
