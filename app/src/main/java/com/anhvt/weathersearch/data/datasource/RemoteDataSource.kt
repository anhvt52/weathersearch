package com.anhvt.weathersearch.data.datasource

import com.anhvt.weathersearch.domain.entity.WeatherDetails

interface RemoteDataSource {
    suspend fun searchByCityName(city: String): List<WeatherDetails>
}