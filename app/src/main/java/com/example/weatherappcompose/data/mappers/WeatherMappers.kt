package com.example.weatherappcompose.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weatherappcompose.data.model.WeatherDataDto
import com.example.weatherappcompose.data.model.WeatherDto
import com.example.weatherappcompose.domain.weather.WeatherData
import com.example.weatherappcompose.domain.weather.WeatherInfo
import com.example.weatherappcompose.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDataDto.toWeatherDataMap():Map<Int,List<WeatherData>>{
    return time.mapIndexed { index, time ->
      val   temperatures= temperatures[index]
        val weatherCodes= weatherCodes[index]
        val pressures= pressures[index]
        val windSpeeds=windSpeeds[index]
        val humidities= humidities[index]
      IndexedWeatherData(
          index = index,
          data = WeatherData(
              time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
              humidity = humidities,
              weatherType = WeatherType.fromWMO(weatherCodes),
              pressure = pressures,
              windSpeed = windSpeeds,
              temperatureCelsius = temperatures
          )
      )

    }.groupBy{
       it.index/24
    }.mapValues {
        it.value.map {
            it.data
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun WeatherDto.toWeatherInfo():WeatherInfo{
   val  weatherDataMap= weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour= if(now.minute<30) now.hour else now.hour+1
        it.time.hour==hour
    }
    return WeatherInfo(
        currentWeatherData = currentWeatherData,
        weatherDataPerDay = weatherDataMap
    )
}