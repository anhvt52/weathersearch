package com.anhvt.weathersearch.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.anhvt.weathersearch.domain.entity.WeatherDetails
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

class SearchWeatherLocalDataSourceImpl @Inject constructor() : LocalDatasource {
    override suspend fun searchByCityName(city: String): List<WeatherDetails> {
        delay(2000) // simulate network delay
        return listOf(
            WeatherDetails(LocalDate.now(), 30f, 70, 50f, "light rain"),
            WeatherDetails(LocalDate.now(), 30f, 60, 60f, "heavy rain"),
            WeatherDetails(LocalDate.now(), 30f, 50, 70f, "sunny")
        )
    }

    override suspend fun update(city: String, data: List<WeatherDetails>) {
        TODO("Not yet implemented")
    }
}