package com.anhvt.weathersearch.presentation.ui

class MainContract {
    sealed class Action {
        data class SubmitClicked(val query: String) : Action()
    }

    sealed class State {
        object Idle : State()
        object Loading : State()
        data class Success(val weatherList: List<WeatherDetailsUi>) : State()
        data class Error(val errorMessage: String) : State()
    }

    sealed class Effect {
        data class NotSufficientLength(val message: String) : Effect()
    }
}