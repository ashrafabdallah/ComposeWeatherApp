package com.example.weatherappcompose.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherappcompose.data.api.WeatherApi
import com.example.weatherappcompose.data.mappers.toWeatherInfo
import com.example.weatherappcompose.domain.repository.WeatherRepository
import com.example.weatherappcompose.domain.util.Resource
import com.example.weatherappcompose.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryIMPL @Inject constructor(
    private val weatherApi: WeatherApi

) : WeatherRepository {


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {

        return try {
            Resource.Success(
                data = weatherApi.getWeatherData(lat,long)
                    .toWeatherInfo()
            )

        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.message ?:"An unknown error occurred.")
        }
    }
}