package com.anhvt.weathersearch.presentation.di

import com.anhvt.weathersearch.data.WeatherRepositoryImpl
import com.anhvt.weathersearch.data.datasource.LocalDatasource
import com.anhvt.weathersearch.data.datasource.RemoteDataSource
import com.anhvt.weathersearch.data.datasource.SearchWeatherLocalDataSourceImpl
import com.anhvt.weathersearch.data.datasource.SearchWeatherRemoteDataSourceImpl
import com.anhvt.weathersearch.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainModule {
    @Binds
    @ViewModelScoped
    abstract fun weatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: SearchWeatherRemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSour(
        localDataSourceImpl: SearchWeatherLocalDataSourceImpl
    ): LocalDatasource
}