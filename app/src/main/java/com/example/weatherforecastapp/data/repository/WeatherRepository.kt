package com.example.weatherforecastapp.data.repository

import com.example.weatherforecastapp.data.api.ApiClient
import com.example.weatherforecastapp.data.model.CityResponse
import com.example.weatherforecastapp.data.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository {
    suspend fun getCityCoordinates(cityName: String): List<CityResponse> {
        return withContext(Dispatchers.IO) {
            ApiClient.cityApiService.getCityCoordinates(cityName)
        }
    }

    suspend fun getWeatherForecast(latitude: Double, longitude: Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            ApiClient.weatherApiService.getWeatherForecast(latitude, longitude)
        }
    }
}