package com.anhvt.weathersearch.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anhvt.weathersearch.R

class MainAdapter() :
    ListAdapter<WeatherDetailsUi, MainAdapter.ViewHolder>(DiffCallback()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        val tvAvgTmp: TextView = view.findViewById(R.id.tv_avg_temp)
        val tvPressure: TextView = view.findViewById(R.id.tv_pressure)
        val tvHumidity: TextView = view.findViewById(R.id.tv_humidity)
        val tvDesc: TextView = view.findViewById(R.id.tv_desc)
    }

    private class DiffCallback : DiffUtil.ItemCallback<WeatherDetailsUi>() {
        override fun areItemsTheSame(oldItem: WeatherDetailsUi, newItem: WeatherDetailsUi): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherDetailsUi, newItem: WeatherDetailsUi): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            tvDate.text = "Date: ${item.date}"
            tvAvgTmp.text = "Average temperature: ${item.averageTemp}"
            tvPressure.text = "Pressure: ${item.pressure}"
            tvHumidity.text = "Hudimity: ${item.humidity}%"
            tvDesc.text = "Description: ${item.desc}"
        }
    }

}

