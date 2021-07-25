package com.anhvt.weathersearch.presentation.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anhvt.weathersearch.domain.entity.WeatherDetails
import com.anhvt.weathersearch.domain.repository.WeatherRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
class MainVMTest {
    private val mockRemoteData = listOf(WeatherDetails(LocalDate.now(), 10f, 10, 10f, "Desc1"))

    @ExperimentalCoroutinesApi

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var mainVM: MainVM

    @Mock
    lateinit var weatherRepository: WeatherRepository

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        weatherRepository = Mockito.mock(WeatherRepository::class.java)
        mainVM = MainVM(weatherRepository)
    }

    @ObsoleteCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `submit insufficient length`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            var result: SearchEffect? = null
            val job = launch {
                result = mainVM.uiEffect.first()
            }
            mainVM.setAction(SearchAction.SubmitClicked("12"))
            job.join()
            assert(result is SearchEffect.NotSufficientLength)
        }
    }

    @Test
    fun `submit sufficient length and get data success`(): Unit = runBlocking {
        launch(Dispatchers.Main) {
            Mockito.`when`(weatherRepository.searchByCityName("saigon"))
                .thenReturn(flowOf(mockRemoteData))
            mainVM.setAction(SearchAction.SubmitClicked("saigon"))
            val states = mainVM.uiState.take(3).toList()
            assert(states[0] is SearchState.Idle)
            assert(states[1] is SearchState.Loading)
            assert(states[2] is SearchState.Success)
        }
    }

    @Test
    fun `submit sufficient length and get data failed`(): Unit = runBlocking {
        Mockito.`when`(weatherRepository.searchByCityName("saigon"))
            .thenReturn(flow { throw Exception() })
        launch(Dispatchers.Main) {
            mainVM.setAction(SearchAction.SubmitClicked("saigon"))
            val states = mainVM.uiState.take(3).toList()
            assert(states[0] is SearchState.Idle)
            assert(states[1] is SearchState.Loading)
            assert(states[2] is SearchState.Error)
        }
    }
}