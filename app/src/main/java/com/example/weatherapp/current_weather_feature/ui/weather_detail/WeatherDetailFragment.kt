package com.example.weatherapp.current_weather_feature.ui.weather_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentWeatherDetailBinding
import com.example.weatherapp.current_weather_feature.other.Resource
import com.example.weatherapp.current_weather_feature.ui.CurrentWeatherViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WeatherDetailFragment : Fragment(R.layout.fragment_weather_detail), OnMapReadyCallback {

    private val viewModel: CurrentWeatherViewModel by activityViewModels()

    private var position = LatLng(0.0, 0.0)

    lateinit var binding: FragmentWeatherDetailBinding

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        viewModel.currentWeather.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { data ->

                        binding.tvName.text = data.location.name
                        binding.tvCountry.text = data.location.country
                        binding.tvRegion.text = data.location.region
                        binding.tvTimezone.text = data.location.timezone_id
                        binding.tvLocalTime.text = data.location.localtime
                        binding.tvTemperature.text = "${data.current.temperature}C"
                        binding.tvWindSpeed.text = data.current.wind_speed.toString()
                        binding.tvWindDir.text = data.current.wind_dir
                        binding.tvHumidity.text = data.current.humidity.toString()
                        position =
                            LatLng(data.location.lat.toDouble(), data.location.lon.toDouble())
                        moveCameraToLocation(map)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun moveCameraToLocation(map: GoogleMap) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                position,
                10f
            )
        )

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.linearLayout.visibility = View.VISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.linearLayout.visibility = View.INVISIBLE
        isLoading = true
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }
}