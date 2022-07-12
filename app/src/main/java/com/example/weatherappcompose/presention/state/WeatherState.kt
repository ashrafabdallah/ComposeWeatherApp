package com.example.weatherappcompose.presention.state

import com.example.weatherappcompose.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo?=null,
    val isLoading:Boolean=false,
    val error:String?=null

)
