package com.manohar.mobiquitycodechallenge.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.data.remote.RetrofitBuilder
import com.manohar.mobiquitycodechallenge.model.WeatherForecastModel
import com.manohar.mobiquitycodechallenge.model.WeatherResponseData
import kotlinx.coroutines.*

class WeatherViewModel : ViewModel() {

    private var _job: Job? = null
    private val _exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception:${throwable.localizedMessage}")
    }

    val weatherResponseData = MutableLiveData<WeatherResponseData>()
    val forecastResponseData = MutableLiveData<WeatherForecastModel>()
    val apiError = MutableLiveData<String?>()
    val loadingIndicator = MutableLiveData<Boolean>()

    fun fetchWeatherData(latitude: String, longitude: String) {
        loadingIndicator.postValue(true)
        _job = CoroutineScope(Dispatchers.IO + _exceptionHandler).launch {
            val response = RetrofitBuilder.apiService.fetchTodayWeather(latitude, longitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        loadingIndicator.postValue(false)
                        weatherResponseData.postValue(response.body())
                        apiError.postValue("")
                    } else {
                        loadingIndicator.postValue(false)
                        apiError.postValue(response.errorBody()?.charStream()?.readText())
                    }
                } else {
                    loadingIndicator.postValue(false)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }


    fun fetchForecastData(latitude: String, longitude: String) {
        loadingIndicator.postValue(true)
        _job = CoroutineScope(Dispatchers.IO + _exceptionHandler).launch {
            val response = RetrofitBuilder.apiService.fetchForecast(latitude, longitude)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        loadingIndicator.postValue(false)
                        forecastResponseData.postValue(response.body())
                        apiError.postValue("")
                    } else {
                        loadingIndicator.postValue(false)
                        apiError.postValue(response.errorBody()?.charStream()?.readText())
                    }
                } else {
                    loadingIndicator.postValue(false)
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        apiError.postValue(message)
        loadingIndicator.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        _job?.cancel()
    }
}