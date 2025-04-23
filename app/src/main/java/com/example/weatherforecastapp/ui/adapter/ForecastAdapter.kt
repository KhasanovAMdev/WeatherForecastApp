package com.example.weatherforecastapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecastapp.R

class ForecastAdapter(
    private val times: List<String>,
    private val temperatures: List<Double>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.time)
        val temperature: TextView = itemView.findViewById(R.id.temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val fullTime = times[position]
        val formattedTime = fullTime.split("T").let {
            if (it.size == 2) {
                "${it[0]} ${it[1].substring(0, 5)}"
            } else {
                fullTime
            }
        }

        holder.time.text = formattedTime
        holder.temperature.text = "${temperatures[position]}°C"
    }

    override fun getItemCount(): Int = minOf(times.size, temperatures.size, 24)
}