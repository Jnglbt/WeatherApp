package com.example.weatherapp.current_weather_feature.ui.weather_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.current_weather_feature.adapters.CurrentWeatherAdapter
import com.example.weatherapp.databinding.FragmentWeatherSearchBinding
import com.example.weatherapp.current_weather_feature.ui.CurrentWeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherSearchFragment : Fragment(R.layout.fragment_weather_search) {

    private val viewModel: CurrentWeatherViewModel by activityViewModels()

    lateinit var currentWeatherAdapter : CurrentWeatherAdapter

    lateinit var binding: FragmentWeatherSearchBinding

    var text: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        currentWeatherAdapter.setOnItemClickListener {
            viewModel.getCurrentWeatherFromDB(it)
            findNavController().navigate(
                R.id.action_weatherSearchFragment_to_weatherDetailFragment,
            )
        }

        binding.btnSearch.setOnClickListener {
            if (validateTextField()) {
                viewModel.getCurrentWeather(text)
                findNavController().navigate(R.id.action_weatherSearchFragment_to_weatherDetailFragment)
            }
            else Toast.makeText(activity, "Enter some text", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateTextField(): Boolean {
        text = binding.etSearch.text.toString()
        return text.isNotEmpty()
    }

    private fun setupRecyclerView() {
        currentWeatherAdapter = CurrentWeatherAdapter(viewModel.getSavedQueries())
        binding.rvQueries.apply {
            adapter = currentWeatherAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}