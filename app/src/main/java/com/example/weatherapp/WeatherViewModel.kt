package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.apirelated.NetworkResponse
import com.example.weatherapp.apirelated.RetrofitInstance
import com.example.weatherapp.apirelated.WeatherApiKey
import com.example.weatherapp.apirelated.WeatherData
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherAPI = RetrofitInstance.weatherAPI

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherData>>()

    val weatherResult : LiveData<NetworkResponse<WeatherData>> = _weatherResult


    fun getWeatherData(place : String){

        _weatherResult.value = NetworkResponse.Loading

        viewModelScope.launch {
           val response =  weatherAPI.getWeather(WeatherApiKey.weatherApiKey,place)

            try {
                if (response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Sucess(it)
                    }
                }else{
                    _weatherResult.value = NetworkResponse.Error("failed to load data")
                }
            }
            catch (e:Exception){
                _weatherResult.value = NetworkResponse.Error("failed to load data")
            }

        }

    }
}