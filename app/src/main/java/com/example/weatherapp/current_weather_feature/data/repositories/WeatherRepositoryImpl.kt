package com.example.weatherapp.current_weather_feature.data.repositories

import com.example.weatherapp.current_weather_feature.data.data_sources.local.WeatherDatabase
import com.example.weatherapp.current_weather_feature.data.data_sources.remote.WeatherStackApi
import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse
import com.example.weatherapp.current_weather_feature.domain.repositories.WeatherRepository
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherStackApi,
    private val db: WeatherDatabase
) : WeatherRepository {

    override suspend fun getCurrentWeather(query: String): Response<CurrentWeatherResponse> =
        api.getCurrentWeather(query)

    override suspend fun saveCurrentWeather(response: CurrentWeatherResponse) =
        db.currentWeatherDao.insertResponse(response)

    override suspend fun getSavedWeatherByQuery(text: String): CurrentWeatherResponse =
        db.currentWeatherDao.getSavedWeatherByQuery("$text%")

    override suspend fun getSavedQueries(): List<String> = db.currentWeatherDao.getSavedQueries()

    override suspend fun getSimilarQueries(text: String): List<String> =
        db.currentWeatherDao.getSimilarQueries("%$text%")

    override suspend fun nukeTable() = db.currentWeatherDao.nukeTable()
}