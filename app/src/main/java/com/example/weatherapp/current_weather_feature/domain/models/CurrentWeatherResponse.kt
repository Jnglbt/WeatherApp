package com.example.weatherapp.current_weather_feature.domain.models

import androidx.room.Embedded
import androidx.room.Entity
import java.io.Serializable

@Entity(
    tableName = "weather_responses",
    primaryKeys = ["query"]
)

data class CurrentWeatherResponse(
    @Embedded val error: ErrorInfo?,
    @Embedded val current: CurrentWeather,
    @Embedded val location: Location,
    @Embedded val request: RequestData
) : Serializable