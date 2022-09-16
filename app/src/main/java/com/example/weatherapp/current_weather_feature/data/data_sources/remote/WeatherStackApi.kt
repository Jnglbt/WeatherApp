package com.example.weatherapp.current_weather_feature.data.data_sources.remote

import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse
import com.example.weatherapp.current_weather_feature.other.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherStackApi {

    @GET("current?access_key=$API_KEY")
    suspend fun getCurrentWeather(@Query("query") param: String) : Response<CurrentWeatherResponse>
}