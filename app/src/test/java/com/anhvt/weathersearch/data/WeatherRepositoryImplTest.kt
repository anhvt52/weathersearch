package com.anhvt.weathersearch.data

import com.anhvt.weathersearch.data.datasource.LocalDatasource
import com.anhvt.weathersearch.data.datasource.RemoteDataSource
import com.anhvt.weathersearch.domain.entity.WeatherDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import java.time.LocalDate

@RunWith(JUnit4::class)
class WeatherRepositoryImplTest {
    private val mockRemoteData = listOf(WeatherDetails(LocalDate.now(), 10f, 10, 10f, "Desc1"))
    private val mockLocalData = listOf(WeatherDetails(LocalDate.now(), 100f, 100, 100f, "Desc2"))


    @Mock
    lateinit var mockLocalDatasource: LocalDatasource

    @Mock
    lateinit var mockRemoteDataSource: RemoteDataSource

    lateinit var weatherRepositoryImpl: WeatherRepositoryImpl
    private val cityName = "test"
    @Before
    fun setUp() {
        mockLocalDatasource = Mockito.mock(LocalDatasource::class.java)
        mockRemoteDataSource = Mockito.mock(RemoteDataSource::class.java)
        weatherRepositoryImpl = WeatherRepositoryImpl(mockRemoteDataSource, mockLocalDatasource)
    }

    @Test
    fun `when local data is valid then return local data`() = runBlockingTest {
        launch {
            Mockito.`when`(mockLocalDatasource.searchByCityName(cityName)).thenReturn(mockLocalData)
            val result = weatherRepositoryImpl.searchByCityName(cityName).first()
            assert(result == mockLocalData)
        }
    }

    @Test
    fun `when local data is not valid then get data from remote source and update local data`() =
        runBlocking {
            val job = launch {
                Mockito.`when`(mockLocalDatasource.searchByCityName(cityName)).thenReturn(emptyList())
                Mockito.`when`(mockRemoteDataSource.searchByCityName(cityName))
                    .thenReturn(mockRemoteData)
                val result = weatherRepositoryImpl.searchByCityName(cityName).first()
                assert(result == mockRemoteData)
                Mockito.verify(mockLocalDatasource, times(1)).update(cityName, mockRemoteData)
            }
            job.join()
        }
}