package com.anhvt.weathersearch.presentation.ui

data class SearchState(
    val isLoading: Boolean = false,
    val weatherList: List<WeatherDetailsUi> = listOf(),
    val errorMessage: String? = null
)