package com.example.weatherapp.current_weather_feature.domain.repositories

import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(query: String) : Response<CurrentWeatherResponse>

    suspend fun saveCurrentWeather(response: CurrentWeatherResponse)

    suspend fun getSavedWeather() : List<CurrentWeatherResponse>

    suspend  fun getSavedWeatherByQuery(text: String): CurrentWeatherResponse

    suspend fun getSavedQueries() : List<String>

    suspend fun nukeTable()
}