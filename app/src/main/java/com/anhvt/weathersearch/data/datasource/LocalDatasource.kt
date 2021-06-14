package com.anhvt.weathersearch.data.datasource

import com.anhvt.weathersearch.domain.entity.WeatherDetails

interface LocalDatasource {
    suspend fun searchByCityName(city: String): List<WeatherDetails>
    suspend fun update(city: String, data:List<WeatherDetails> )
}