package com.example.weatherforecastapp.ui.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.data.model.City
import com.example.weatherforecastapp.data.model.WeatherResponse

class CitiesAdapter(
    private var cities: List<City>,
    private var weatherData: Map<String, WeatherResponse>,
    private val onRemoveClick: (String) -> Unit
) : RecyclerView.Adapter<CitiesAdapter.CityViewHolder>() {

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityName)
        val currentTemp: TextView = itemView.findViewById(R.id.currentTemp)
        val forecastRecyclerView: RecyclerView = itemView.findViewById(R.id.forecastRecyclerView)
        val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.cityName.text = city.name

        val weather = weatherData[city.name]
        if (weather != null) {
            if (weather.hourly.temperature_2m.isNotEmpty()) {
                val currentTemp = weather.hourly.temperature_2m[0]
                holder.currentTemp.text = "Текущая температура: ${currentTemp}°C"
            }

            val forecastAdapter = ForecastAdapter(
                times = weather.hourly.time,
                temperatures = weather.hourly.temperature_2m
            )
            holder.forecastRecyclerView.layoutManager = LinearLayoutManager(
                holder.itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            holder.forecastRecyclerView.adapter = forecastAdapter
        } else {
            holder.currentTemp.text = "Загрузка данных..."
        }

        holder.removeButton.setOnClickListener {
            onRemoveClick(city.name)
        }
    }

    override fun getItemCount(): Int = cities.size

    fun updateData(newCities: List<City>, newWeatherData: Map<String, WeatherResponse>) {
        cities = newCities
        weatherData = newWeatherData.toMap()
        notifyDataSetChanged()
    }
}