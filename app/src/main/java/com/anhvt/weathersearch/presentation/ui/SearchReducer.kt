package com.anhvt.weathersearch.presentation.ui

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SearchReducer @Inject constructor() {
    fun reduce(currentState: SearchState, searchAction: SearchAction): SearchState {
        return when (searchAction) {
            is SearchAction.StartSearching -> {
                currentState.copy(isLoading = true)
            }
            is SearchAction.Loaded -> {
                currentState.copy(isLoading = false, weatherList = searchAction.weatherList)
            }
            is SearchAction.Error -> {
                currentState.copy(isLoading = false, errorMessage = searchAction.message)
            }
        }
    }
}