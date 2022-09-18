package com.example.weatherapp.current_weather_feature.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.current_weather_feature.domain.models.CurrentWeatherResponse
import com.example.weatherapp.current_weather_feature.domain.repositories.WeatherRepository
import com.example.weatherapp.current_weather_feature.other.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(
    @ApplicationContext app: Context,
    private val weatherRepository: WeatherRepository,
) : AndroidViewModel(app as Application) {

    val currentWeather: MutableLiveData<Resource<CurrentWeatherResponse>> = MutableLiveData()
    var currentWeatherResponse: CurrentWeatherResponse? = null

    fun getCurrentWeather(query: String) = viewModelScope.launch() {

        try {
            if (hasInternetConnection())
                safeCurrentWeatherCall(query)
            else {
                getCurrentWeatherFromDB(query)
            }
        } catch (t: Throwable) {
            currentWeather.postValue(Resource.Error("No internet connection."))
        }
    }

    private suspend fun safeCurrentWeatherCall(query: String) {
        currentWeather.postValue(Resource.Loading())
        try {
            val response = weatherRepository.getCurrentWeather(query)
            currentWeather.postValue(handleCurrentWeatherResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> currentWeather.postValue(Resource.Error("Network Failure."))
                else -> currentWeather.postValue(Resource.Error(t.message ?: "Conversion error."))
            }
        }
    }

    private fun handleCurrentWeatherResponse(
        response: Response<CurrentWeatherResponse>
    ): Resource<CurrentWeatherResponse> {
        if (response.body()!!.error == null && response.isSuccessful) {
            response.body()?.let { result ->
                currentWeatherResponse = result
                saveCurrentWeather(result)
                return Resource.Success(currentWeatherResponse ?: result)
            }
        }
        return Resource.Error(response.body()!!.error?.info ?: response.message())
    }

    fun getCurrentWeatherFromDB(query: String): Resource<CurrentWeatherResponse> {

        runBlocking {
            currentWeatherResponse = weatherRepository.getSavedWeatherByQuery(query)
            currentWeather.postValue(Resource.Success(currentWeatherResponse!!))
        }
        return Resource.Error("Error message")
    }

    private fun saveCurrentWeather(response: CurrentWeatherResponse) = viewModelScope.launch {
        weatherRepository.saveCurrentWeather(response)
    }

    fun getSavedQueries(): List<String> = runBlocking {
        weatherRepository.getSavedQueries()
    }

    fun getSimilarQueries(text: String): List<String> = runBlocking {
        weatherRepository.getSimilarQueries(text)
    }


//    fun nukeTable() = viewModelScope.launch {
//        weatherRepository.nukeTable()
//    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<WeatherApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}
