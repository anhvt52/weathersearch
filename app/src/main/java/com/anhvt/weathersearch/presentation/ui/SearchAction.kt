package com.anhvt.weathersearch.presentation.ui

sealed class SearchAction {
    object StartSearching : SearchAction()
    data class Loaded(val weatherList: List<WeatherDetailsUi>) : SearchAction()
    data class Error(val message: String) : SearchAction()
}