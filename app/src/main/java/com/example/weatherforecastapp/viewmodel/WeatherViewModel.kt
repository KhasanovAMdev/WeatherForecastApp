package com.example.weatherforecastapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.data.model.City
import com.example.weatherforecastapp.data.model.WeatherResponse
import com.example.weatherforecastapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    private val _weatherData = MutableStateFlow<Map<String, WeatherResponse>>(emptyMap())
    val weatherData: StateFlow<Map<String, WeatherResponse>> = _weatherData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun addCity(cityName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val cityResponse = repository.getCityCoordinates(cityName)
                if (cityResponse.isNotEmpty()) {
                    val city = City(
                        name = cityResponse[0].name,
                        latitude = cityResponse[0].latitude,
                        longitude = cityResponse[0].longitude
                    )

                    _cities.value = _cities.value + city
                    fetchWeatherForCity(city)
                } else {
                    _error.value = "Город не найден"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка при получении координат города: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchWeatherForCity(city: City) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val weather = repository.getWeatherForecast(city.latitude, city.longitude)
                _weatherData.value = _weatherData.value + (city.name to weather)
            } catch (e: Exception) {
                _error.value = "Ошибка при получении погоды для ${city.name}: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun removeCity(cityName: String) {
        _cities.value = _cities.value.filter { it.name != cityName }
        _weatherData.value = _weatherData.value.filterKeys { it != cityName }
    }

    class WeatherViewModelFactory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                return WeatherViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}