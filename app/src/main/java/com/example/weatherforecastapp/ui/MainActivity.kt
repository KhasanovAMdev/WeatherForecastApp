package com.example.weatherforecastapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.repository.WeatherRepository
import com.example.weatherforecastapp.ui.adapter.CitiesAdapter
import com.example.weatherforecastapp.viewmodel.WeatherViewModel
import com.example.weatherforecastapp.viewmodel.WeatherViewModel.WeatherViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory(WeatherRepository())
    }

    private lateinit var cityInput: EditText
    private lateinit var addCityButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var citiesRecyclerView: RecyclerView
    private lateinit var citiesAdapter: CitiesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()
        citiesRecyclerView.layoutManager = LinearLayoutManager(this)
        setupAdapter()
        setupObservers()
        setupListeners()
    }

    private fun initViews() {
        cityInput = findViewById(R.id.cityInput)
        addCityButton = findViewById(R.id.addCityButton)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.errorText)
        citiesRecyclerView = findViewById(R.id.citiesRecyclerView)
    }

    private fun setupAdapter() {
        citiesAdapter = CitiesAdapter(
            cities = emptyList(),
            weatherData = emptyMap(),
            onRemoveClick = { cityName ->
                viewModel.removeCity(cityName)
            }
        )
        citiesRecyclerView.adapter = citiesAdapter
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.cities.collect { cities ->
                citiesAdapter.updateData(cities, viewModel.weatherData.value)
            }
        }

        lifecycleScope.launch {
            viewModel.weatherData.collect { weatherData ->
                citiesAdapter.updateData(viewModel.cities.value, weatherData)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect { error ->
                errorText.text = error
                errorText.visibility = if (error != null) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupListeners() {
        addCityButton.setOnClickListener {
            val cityName = cityInput.text.toString().trim()
            if (cityName.isNotEmpty()) {
                viewModel.addCity(cityName)
                cityInput.text.clear()
            }
        }
    }
}