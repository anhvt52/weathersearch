package com.anhvt.weathersearch.data

import com.anhvt.weathersearch.data.datasource.LocalDatasource
import com.anhvt.weathersearch.data.datasource.RemoteDataSource
import com.anhvt.weathersearch.domain.entity.WeatherDetails
import com.anhvt.weathersearch.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDatasource: LocalDatasource
) : WeatherRepository {
    override fun searchByCityName(cityName: String): Flow<List<WeatherDetails>> {
        return flow {
            val local = localDatasource.searchByCityName(cityName)
            if (local.isNotEmpty()) {
                emit(local)
                return@flow
            }
            val remote = remoteDataSource.searchByCityName(cityName)
            withContext(Dispatchers.IO) {
                localDatasource.update(cityName, remote)
            }
            emit(remote)
            return@flow
        }
    }
}