package com.anhvt.weathersearch.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anhvt.weathersearch.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainContract.State>(MainContract.State.Idle)
    val uiState
        get() = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<MainContract.Effect?>()
    val uiEffect
        get() = _uiEffect.asSharedFlow()

    private val _action = MutableSharedFlow<MainContract.Action>()
    private val uiAction = _action.asSharedFlow()

    init {
        listenAction()
    }

    @OptIn(FlowPreview::class)
    private fun listenAction() {
        viewModelScope.launch {
            uiAction.sample(1000) // prevent fast-click
                .collect {
                    handleAction(it)
                }
        }
    }

    private suspend fun handleAction(action: MainContract.Action) {
        when (action) {
            is MainContract.Action.SubmitClicked -> {
                search(action.query.trim())
            }
        }
    }

    private suspend fun search(query: String) {
        if (query.length < 3) {
            _uiEffect.emit(
                MainContract.Effect.NotSufficientLength("Length should be bigger or equal 3")
            )
        } else {
            _uiState.value = MainContract.State.Loading
            weatherRepository
                .searchByCityName(query)
                .flowOn(Dispatchers.IO)
                .map { data ->
                    data.map { weatherDetails -> weatherDetails.mapToUiModel() }
                }
                .catch { error ->
                    _uiState.value = MainContract.State.Error(errorMessage = error.message ?: "")
                }
                .collect {
                    _uiState.value =
                        MainContract.State.Success(it)
                }
        }
    }

    fun setAction(action: MainContract.Action) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    companion object {
        private const val TAG = "MainVM"
    }
}

