package com.anhvt.weathersearch.presentation.ui

import com.anhvt.weathersearch.domain.entity.WeatherDetails
import java.time.format.DateTimeFormatter

data class WeatherDetailsUi(
    val date: String,
    val averageTemp: String,
    val pressure: String,
    val humidity: String,
    val desc: String
)

fun WeatherDetails.mapToUiModel(): WeatherDetailsUi = WeatherDetailsUi(
    date = date.format(DateTimeFormatter.ofPattern("E, dd LLL yyyy")),
    averageTemp = averageTemperature.toString(),
    pressure = pressure.toString(),
    humidity = humidity.toString(),
    desc = description
)