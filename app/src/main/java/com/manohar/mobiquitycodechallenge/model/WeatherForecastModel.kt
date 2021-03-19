package com.manohar.mobiquitycodechallenge.model

data class WeatherForecastModel(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: ArrayList<ForecastPrediction>?,
    val message: Int
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class ForecastPrediction(
    val clouds: Clouds,
    val dt: Long,
    val dt_txt: String,
    val main: MainForecast,
    val pop: Int,
    val sys: Syste,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

data class MainForecast(
    val feels_like: Double,
    val grnd_level: Double,
    val humidity: Double,
    val pressure: Double,
    val sea_level: Double,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Syste(
    val pod: String
)

