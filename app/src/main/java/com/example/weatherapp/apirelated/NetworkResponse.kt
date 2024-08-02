package com.example.weatherapp.apirelated

sealed class NetworkResponse<out T> { // T refers to weather model

    data class Sucess <out T>(val data: T): NetworkResponse<T>()
    data class Error (val message: String): NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}