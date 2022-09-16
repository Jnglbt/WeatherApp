package com.example.weatherapp.current_weather_feature.data.data_sources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse

@Database(
    entities = [CurrentWeatherResponse::class],
    version = 1,
)

@TypeConverters(Converters::class)

abstract class WeatherDatabase : RoomDatabase() {

    abstract val currentWeatherDao : CurrentWeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }
}