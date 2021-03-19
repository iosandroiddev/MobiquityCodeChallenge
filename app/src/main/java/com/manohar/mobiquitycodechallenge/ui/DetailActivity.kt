package com.manohar.mobiquitycodechallenge.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.manohar.mobiquitycodechallenge.R
import com.manohar.mobiquitycodechallenge.model.BookmarkLocationModel
import com.manohar.mobiquitycodechallenge.model.ForecastPrediction
import com.manohar.mobiquitycodechallenge.model.WeatherForecastModel
import com.manohar.mobiquitycodechallenge.model.WeatherResponseData
import com.manohar.mobiquitycodechallenge.ui.adapter.WeatherForecastAdapter
import com.manohar.mobiquitycodechallenge.ui.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var _pickedLocationData: BookmarkLocationModel? = null

    private lateinit var _weatherViewModel: WeatherViewModel
    private lateinit var _forecastAdapter: WeatherForecastAdapter

    private var _arrayForecastPrediction = ArrayList<ForecastPrediction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.location_details)
        }
        setUpRecyclerView()
        initVM()
        bindData()
        bindClicks()
    }

    private fun setUpRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        rvForecasts.layoutManager = mLayoutManager
        rvForecasts.itemAnimator = DefaultItemAnimator()
        _forecastAdapter = WeatherForecastAdapter(this)
        rvForecasts.adapter = _forecastAdapter

    }


    private fun bindClicks() {
        buttonFetchForecast.setOnClickListener {
            _pickedLocationData?.let {
                _weatherViewModel.fetchForecastData(it.locationLatitude, it.locationLongitude)
            }
        }
    }

    private fun initVM() {
        _weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        observeVM()
    }

    private fun observeVM() {
        _weatherViewModel.weatherResponseData.observe(this, { apiResponse ->
            apiResponse?.let {
                handleWeatherResponse(it)
            }

        })

        _weatherViewModel.forecastResponseData.observe(this, { apiResponse ->
            apiResponse?.let {
                handleForecastResponse(it)
            }

        })

        _weatherViewModel.apiError.observe(this, { errorMessage ->
            errorMessage?.let {
                if (it.isNotEmpty()) {
                    println("Error:$it")
                }
            }

        })

        _weatherViewModel.loadingIndicator.observe(this, {

        })
    }

    private fun handleForecastResponse(weatherForecastModel: WeatherForecastModel) {
        weatherForecastModel.list?.let {
            _arrayForecastPrediction = it
            _forecastAdapter.addItems(_arrayForecastPrediction)
        }
    }

    private fun handleWeatherResponse(it: WeatherResponseData) {
        tvLocationName.text = "City Name : " + it.name
        if (it.weather.isNotEmpty()) {
            tvWeatherInfo.text = "Weather :  ${it.weather[0].description}"
        }
        tvAdditionalInfo.text =
            "Humidity: " + it.main.humidity + " \n\nTemperature: " + it.main.temp + "\n\nWind Speed: " + it.wind.speed
    }

    private fun bindData() {
        intent.extras?.let { bundleExtras ->
            if (bundleExtras.containsKey("locationData")) {
                _pickedLocationData =
                    bundleExtras.getSerializable("locationData") as BookmarkLocationModel?
                _pickedLocationData?.let {
                    _weatherViewModel.fetchWeatherData(it.locationLatitude, it.locationLongitude)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}