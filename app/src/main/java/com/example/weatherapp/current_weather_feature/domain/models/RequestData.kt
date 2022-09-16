package com.example.weatherapp.current_weather_feature.domain.models

data class RequestData(
    val language: String,
    val query: String,
    val type: String,
    val unit: String
)