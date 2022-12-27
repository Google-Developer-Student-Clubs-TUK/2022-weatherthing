package com.example.weatherthing.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherthing.data.WeatherData
import com.example.weatherthing.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _weather = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weather: StateFlow<WeatherState> = _weather

    fun getCurrentWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _weather.value = WeatherState.Loading

            _weather.value = kotlin.runCatching {
                WeatherRepository.getWeather(latitude, longitude)
            }.mapCatching {
                WeatherState.Loaded(it)
            }.getOrElse {
                WeatherState.Error(it.toString())
            }
        }
    }
}

sealed class WeatherState {
    object Loading : WeatherState()
    class Loaded(val data: WeatherData) : WeatherState()
    class Error(val message: String) : WeatherState()
}
