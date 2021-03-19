package com.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitBuilder {

    const val baseApiUrl = "https://api.openweathermap.org/data/2.5/"

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder().baseUrl(baseApiUrl).addConverterFactory(
            GsonConverterFactory.create(gson)
        )
    }

    val apiService: ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }
}