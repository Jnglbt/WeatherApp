package com.example.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.example.weatherapp.current_weather_feature.data.data_sources.local.WeatherDatabase
import com.example.weatherapp.current_weather_feature.data.data_sources.remote.WeatherStackApi
import com.example.weatherapp.current_weather_feature.data.repositories.WeatherRepositoryImpl
import com.example.weatherapp.current_weather_feature.domain.repositories.WeatherRepository
import com.example.weatherapp.current_weather_feature.other.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWeatherStackApi() : WeatherStackApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherStackApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherDatabase(app: Application) : WeatherDatabase {
        return Room.databaseBuilder(
            app,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCurrentWeatherRepository(api: WeatherStackApi, db: WeatherDatabase) : WeatherRepository {
        return WeatherRepositoryImpl(api, db)
    }
}