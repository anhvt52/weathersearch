package com.anhvt.weathersearch.presentation.ui

sealed class SearchEffect {
    data class NotSufficientLength(val message: String) : SearchEffect()
}