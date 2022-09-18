package com.example.weatherapp.current_weather_feature.data.data_sources.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class Converters {

    @TypeConverter
    fun fromList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        return try {
            Gson().fromJson<List<String>>(value)
        } catch (e: Exception) {
            listOf()
        }
    }

    @TypeConverter
    fun fromFloat(value: Float): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toFloat(value: String): Float {
        return Gson().fromJson<Float>(value)
    }
}

private fun <T> Gson.fromJson(json: String) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)
