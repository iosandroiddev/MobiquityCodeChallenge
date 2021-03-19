package com.data.remote

import com.manohar.mobiquitycodechallenge.model.WeatherForecastModel
import com.manohar.mobiquitycodechallenge.model.WeatherResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @Headers("Content-Type: application/json")
    @GET("weather?appid=fae7190d7e6433ec3a45285ffcf55c86")
    suspend fun fetchTodayWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "metrics"
    ): Response<WeatherResponseData>


    @Headers("Content-Type: application/json")
    @GET("forecast?appid=fae7190d7e6433ec3a45285ffcf55c86&")
    suspend fun fetchForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("units") units: String = "metrics"
    ): Response<WeatherForecastModel>


}