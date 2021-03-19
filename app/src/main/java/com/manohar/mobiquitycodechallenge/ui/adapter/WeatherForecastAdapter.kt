package com.manohar.mobiquitycodechallenge.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manohar.mobiquitycodechallenge.databinding.ItemForecastBinding
import com.manohar.mobiquitycodechallenge.model.ForecastPrediction
import com.manohar.mobiquitycodechallenge.utils.getFormattedDate
import kotlinx.android.synthetic.main.activity_detail.*

class WeatherForecastAdapter(private val context: Context) :
    RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHoler>() {

    private var _pinnedLocationsArray = ArrayList<ForecastPrediction>()


    fun addItems(array: ArrayList<ForecastPrediction>) {
        _pinnedLocationsArray = array
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return _pinnedLocationsArray.size
    }

    fun getItem(position: Int): ForecastPrediction {
        return _pinnedLocationsArray[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHoler {
        return WeatherForecastViewHoler(
            ItemForecastBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHoler, position: Int) {
        val forecastPrediction = getItem(position)
        holder.binding.dataModel = forecastPrediction

        holder.binding.tvDate.text = context.getFormattedDate(forecastPrediction.dt)

        if (forecastPrediction.weather.isNotEmpty()) {
            holder.binding.tvWeatherInfo.text = "Weather :  ${forecastPrediction.weather[0].description}"
        }
        holder.binding.tvAdditionalInfo.text =
            "Humidity: " + forecastPrediction.main.humidity + " \n\nTemperature: " + forecastPrediction.main.temp + "\n\nWind Speed: " + forecastPrediction.wind.speed
    }

    inner class WeatherForecastViewHoler(var binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
        }
    }

}