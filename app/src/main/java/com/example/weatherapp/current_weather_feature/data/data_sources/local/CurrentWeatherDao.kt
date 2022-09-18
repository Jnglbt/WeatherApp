package com.example.weatherapp.current_weather_feature.data.data_sources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse

@Dao
interface CurrentWeatherDao {

    @Query("SELECT `query` FROM weather_responses")
    suspend  fun getSavedQueries(): List<String>

    @Query("SELECT * FROM weather_responses WHERE `query` LIKE (:text)")
    suspend  fun getSavedWeatherByQuery(text: String): CurrentWeatherResponse

    @Query("SELECT `query` FROM weather_responses WHERE `query` LIKE :text")
    suspend fun getSimilarQueries(text: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResponse(response: CurrentWeatherResponse)

    @Query("DELETE FROM weather_responses")
    suspend fun nukeTable()
}