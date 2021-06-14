package com.anhvt.weathersearch.data.datasource

import com.anhvt.weathersearch.domain.entity.WeatherDetails
import javax.inject.Inject

class SearchWeatherRemoteDataSourceImpl @Inject constructor(): RemoteDataSource {
    override suspend fun searchByCityName(city: String): List<WeatherDetails> {
        TODO("Not yet implemented")
    }
}