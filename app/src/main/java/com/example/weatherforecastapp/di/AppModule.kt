package com.example.weatherforecastapp.di

import com.example.weatherforecastapp.data.repository.WeatherRepository
import com.example.weatherforecastapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { WeatherRepository() }
    viewModel { WeatherViewModel(get()) }
}