package com.anhvt.weathersearch.domain.repository

import com.anhvt.weathersearch.domain.entity.WeatherDetails
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun searchByCityName(cityName: String): Flow<List<WeatherDetails>>
}