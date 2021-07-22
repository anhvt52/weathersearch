package com.anhvt.weathersearch.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anhvt.weathersearch.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    @Inject
    lateinit var reducer: SearchReducer

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val currentState
        get() = _state.value

    private val _uiEffect = MutableSharedFlow<SearchEffect?>()
    val uiEffect = _uiEffect.asSharedFlow()

    fun search(query: String) {
        viewModelScope.launch {
            if (query.length < 3) {
                _uiEffect.emit(
                    SearchEffect.NotSufficientLength("Length should be bigger or equal 3")
                )
            } else {
                dispatch(SearchAction.StartSearching)
                weatherRepository
                    .searchByCityName(query)
                    .flowOn(Dispatchers.IO)
                    .map { data ->
                        data.map { weatherDetails -> weatherDetails.mapToUiModel() }
                    }
                    .catch { error ->
                        dispatch(SearchAction.Error(error.message!!))
                    }
                    .collect {
                        dispatch(SearchAction.Loaded(it))
                    }
            }
        }
    }

    private fun dispatch(searchAction: SearchAction) {
        val newState = reducer.reduce(currentState, searchAction)
        _state.value = newState
    }

    companion object {
        private const val TAG = "MainVM"
    }
}

