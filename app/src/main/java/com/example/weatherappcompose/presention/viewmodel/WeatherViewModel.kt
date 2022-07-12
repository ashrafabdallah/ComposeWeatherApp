package com.example.weatherappcompose.presention.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappcompose.domain.location.LocationTracker
import com.example.weatherappcompose.domain.repository.WeatherRepository
import com.example.weatherappcompose.domain.util.Resource
import com.example.weatherappcompose.presention.state.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker

) : ViewModel() {

    var state by mutableStateOf(
        WeatherState()
    )
        private set


    fun loadWeatherLocation() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null,
                weatherInfo = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when (val result =
                    weatherRepository.getWeatherData(location.latitude, location.longitude)) {

                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            error = null,
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            error = result.message,
                            isLoading = false
                        )
                    }

                }

            } ?: kotlin.run {
                state=state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}