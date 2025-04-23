package com.example.weatherforecastapp.data.model

data class WeatherResponse(
    val latitude: Double,
    val longitude: Double,
    val hourly: HourlyData
)

data class HourlyData(
    val time: List<String>,
    val temperature_2m: List<Double>
)